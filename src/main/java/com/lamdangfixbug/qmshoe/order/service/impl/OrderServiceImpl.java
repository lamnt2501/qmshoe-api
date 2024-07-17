package com.lamdangfixbug.qmshoe.order.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.InsufficientInventoryException;
import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderItem;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatusTracking;
import com.lamdangfixbug.qmshoe.order.entity.embedded.OrderItemPK;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderItemRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.repository.OrderRepository;
import com.lamdangfixbug.qmshoe.order.repository.OrderStatusTrackingRepository;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import com.lamdangfixbug.qmshoe.product.repository.ProductOptionRepository;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.repository.AddressRepository;
import com.lamdangfixbug.qmshoe.utils.Utils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OrderStatusTrackingRepository orderStatusTrackingRepository;
    private final AddressRepository addressRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductOptionRepository productOptionRepository, OrderStatusTrackingRepository orderStatusTrackingRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.orderStatusTrackingRepository = orderStatusTrackingRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Order> getAllOrders(Map<String, Object> params) {
        Pageable pageable = Utils.buildPageable(params);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().isEmpty()) {
            return orderRepository.findByCustomer(((Customer) userDetails).getId(), pageable).getContent();
        }
        return orderRepository.findAll(pageable).getContent();
    }

    @Override
    public Order getOrder(int orderId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().isEmpty()) {
            return orderRepository.findByIdAndCustomer_Id(orderId, ((Customer) userDetails).getId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        }
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Could not find order with id " + orderId));
    }

    @Override
    @Transactional
    public Order placeOrder(OrderRequest orderRequest) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Order order = Order.builder().status(OrderStatus.WAITING).customer(customer).build();

        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest oir : orderRequest.getItems()) {
            ProductOption productOption = productOptionRepository.findBySku(oir.getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));

            // out of stock
            if (productOption.getQuantity() == 0) {
                throw new InsufficientInventoryException(productOption.getSku() + " is out of stock");
            }

            // check order quantity is larger than inventory
            if (oir.getQuantity() > productOption.getQuantity()) {
                throw new InsufficientInventoryException(productOption.getSku() + " inventory is not enough");
            }

            orderItems.add(
                    OrderItem.builder()
                            .id(
                                    OrderItemPK.builder()
                                            .sku(oir.getSku())
                                            .orderId(order.getId())
                                            .build()
                            )
                            .quantity(oir.getQuantity())
                            .build()
            );
            // update inventory
            productOption.setQuantity(productOption.getQuantity() - oir.getQuantity());
            productOptionRepository.save(productOption);
            //update total
            // todo: them giam gia cho don hang voi voucher va theo giam gia san pham
            order.setTotal(order.getTotal() + oir.getQuantity() * productOption.getProduct().getPrice());
        }

        order.setOrderItems(orderItems);

        // tracking order status
        List<OrderStatusTracking> tracking = new ArrayList<>();
        tracking.add(orderStatusTrackingRepository.save(OrderStatusTracking.builder()
                .orderId(order.getId())
                .message("Your order is being processed!")
                .status(order.getStatus())
                .build()))
        ;
        order.setAddress(addressRepository.save(Address.builder()
                .city(orderRequest.getAddress().getCity())
                .district(orderRequest.getAddress().getDistrict())
                .specificAddress(orderRequest.getAddress().getSpecificAddress())
                .customerId(customer.getId())
                .build()));
        order.setOrderStatusTracking(tracking);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(UpdateOrderStatusRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order;
        if (userDetails.getAuthorities().isEmpty()) {
            order = orderRepository.findByIdAndCustomer_Id(request.getId(), ((Customer) userDetails).getId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        } else {
            order = orderRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        }

        if (userDetails.getAuthorities().isEmpty()) {
            if (request.getStatus() != OrderStatus.CANCEL || order.getStatus().getPriority() > OrderStatus.WAITING.getPriority()) {
                throw new AccessDeniedException("You don't have permission to update this order");
            }
        }

        if (request.getStatus().getPriority() <= order.getStatus().getPriority()) {
            throw new IllegalArgumentException("Invalid order status");
        }

        if (request.getStatus() == OrderStatus.CANCEL) {
            if (request.getMessage() == null) {
                throw new RuntimeException("You must state the reason for canceling order!");
            }
            for (OrderItem oi : order.getOrderItems()) {
                ProductOption productOption = productOptionRepository.findBySku(oi.getId().getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));
                productOption.setQuantity(productOption.getQuantity() + oi.getQuantity());
                productOptionRepository.save(productOption);
            }
        }

        order.setStatus(request.getStatus());
        String message = request.getMessage();

        if (message == null) {
            message = switch (request.getStatus()) {
                case SUCCEEDED -> "Order delivered succeed!";
                case SHIPPING -> "Your order is being shipped to you!";
                case APPROVED -> "Your order is approved!";
                default -> "";
            };
        }
        orderStatusTrackingRepository.save(OrderStatusTracking.builder()
                .orderId(order.getId())
                .message(message)
                .status(request.getStatus())
                .build());
        return orderRepository.save(order);
    }
}
