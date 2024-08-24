package com.lamdangfixbug.qmshoe.order.service.impl;

import com.lamdangfixbug.qmshoe.cart.entity.Cart;
import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import com.lamdangfixbug.qmshoe.cart.entity.embedded.CartItemPK;
import com.lamdangfixbug.qmshoe.cart.repository.CartItemRepository;
import com.lamdangfixbug.qmshoe.cart.repository.CartRepository;
import com.lamdangfixbug.qmshoe.exceptions.InsufficientInventoryException;
import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.exceptions.UpdateOrderException;
import com.lamdangfixbug.qmshoe.order.entity.*;
import com.lamdangfixbug.qmshoe.order.entity.embedded.OrderItemPK;
import com.lamdangfixbug.qmshoe.order.payload.request.AddressRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderItemRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.payload.response.OrderSummary;
import com.lamdangfixbug.qmshoe.order.payload.response.ProductBestSellerResponse;

import com.lamdangfixbug.qmshoe.order.payload.response.TopCustomerResponse;
import com.lamdangfixbug.qmshoe.order.repository.OrderRepository;
import com.lamdangfixbug.qmshoe.order.repository.OrderStatusTrackingRepository;
import com.lamdangfixbug.qmshoe.order.repository.ProductBestSellerRepository;
import com.lamdangfixbug.qmshoe.order.repository.TopCustomerRepository;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentStatus;
import com.lamdangfixbug.qmshoe.payment.repository.PaymentDetailsRepository;

import com.lamdangfixbug.qmshoe.product.entity.ProductOption;

import com.lamdangfixbug.qmshoe.product.repository.ProductOptionRepository;
import com.lamdangfixbug.qmshoe.product.repository.ProductRepository;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.repository.AddressRepository;
import com.lamdangfixbug.qmshoe.utils.Utils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OrderStatusTrackingRepository orderStatusTrackingRepository;
    private final AddressRepository addressRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductBestSellerRepository productBestSellerRepository;
    private final ProductRepository productRepository;
    private final TopCustomerRepository topCustomerRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductOptionRepository productOptionRepository, OrderStatusTrackingRepository orderStatusTrackingRepository, AddressRepository addressRepository, PaymentDetailsRepository paymentDetailsRepository, CartItemRepository cartItemRepository, CartRepository cartRepository, ProductBestSellerRepository productBestSellerRepository, ProductRepository productRepository, TopCustomerRepository topCustomerRepository) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.orderStatusTrackingRepository = orderStatusTrackingRepository;
        this.addressRepository = addressRepository;
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productBestSellerRepository = productBestSellerRepository;
        this.productRepository = productRepository;
        this.topCustomerRepository = topCustomerRepository;
    }

    @Override
    public boolean orderExists(int orderId) {
        return orderRepository.existsById(orderId);
    }

    @Override
    public List<Order> getAllOrders(Map<String, Object> params) {
        Pageable pageable = Utils.buildPageable(params);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByCustomer(((Customer) userDetails).getId(), pageable).getContent();
    }

    @Override
    public Order getOrder(int orderId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByIdAndCustomer_Id(orderId, ((Customer) userDetails).getId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    @Transactional
    public Order placeOrder(OrderRequest orderRequest) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Order order = Order.builder().status(OrderStatus.WAITING).customer(customer).build();

        String phoneNumber = orderRequest.getPhoneNumber();
        order.setPhoneNumber(phoneNumber == null ? customer.getPhoneNumber() : phoneNumber);
        String receiverName = orderRequest.getReceiverName();
        order.setReceiverName(receiverName == null ? customer.getName() : receiverName);
        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        // order items
        for (OrderItemRequest oir : orderRequest.getItems()) {
            ProductOption productOption = productOptionRepository.findBySku(oir.getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));

            // out of stock
            if (productOption.getQuantity() == 0) {
                throw new InsufficientInventoryException(productOption.getSku() + " is out of stock");
            }

            // check order quantity is larger than inventory
            if (oir.getQuantity() > productOption.getQuantity()) {
                throw new InsufficientInventoryException(productOption.getProduct().getName() + " inventory is not enough");
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
            order.setTotal(order.getTotal() + oir.getQuantity() * productOption.getPrice());

            // update cart
            cartItemRepository.deleteById(
                    CartItemPK.builder().cartId(cart.getId()).sku(oir.getSku()).build()
            );
        }

        order.setOrderItems(orderItems);

        // tracking order status
        List<OrderStatusTracking> tracking = new ArrayList<>();
        tracking.add(orderStatusTrackingRepository.save(OrderStatusTracking.builder()
                .orderId(order.getId())
                .message("Your order is being processed!")
                .status(order.getStatus())
                .build()));

        // address for order
        Address address;
        AddressRequest addressRequest = orderRequest.getAddress();
        if (addressRequest != null) {
            String city = addressRequest.getCity();
            String district = addressRequest.getDistrict();
            String ward = addressRequest.getWard();
            String specificAddress = addressRequest.getSpecificAddress();
            address = addressRepository.findByCustomerIdAndCityLikeIgnoreCaseAndDistrictLikeIgnoreCaseAndWardLikeIgnoreCaseAndSpecificAddressLikeIgnoreCase(
                    customer.getId(), city, district, ward, specificAddress
            ).orElse(null);
            if (address == null) address = addressRepository.save(Address.builder()
                    .city(city).district(district).ward((ward)).specificAddress(specificAddress).customerId(customer.getId()).build());
        } else {
            address = addressRepository.findByCustomerIdAndId(customer.getId(), orderRequest.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        }


        order.setAddress(address);
        order.setOrderStatusTracking(tracking);

        // transaction(payment) for order
        PaymentDetails paymentDetails = paymentDetailsRepository.save(PaymentDetails.builder()
                .amount(order.getTotal())
                .description(customer.getName() + " thanh toan don hang " + order.getId())
                .status(orderRequest.getPaymentMethod().getName().equalsIgnoreCase("COD") ? PaymentStatus.UNPAID : PaymentStatus.PROCESSING)
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderId(order.getId())
                .build()
        );
        order.setPaymentDetails(paymentDetails);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(UpdateOrderStatusRequest request) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository.findByIdAndCustomer_Id(request.getId(), customer.getId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (request.getStatus() != OrderStatus.CANCEL || order.getStatus().getPriority() > 0) {
            throw new UpdateOrderException("Invalid order status");
        } else if (request.getMessage() == null || request.getMessage().isEmpty()) {
            throw new UpdateOrderException("You must state the reason for canceling order!");
        }

        for (OrderItem oi : order.getOrderItems()) {
            ProductOption productOption = productOptionRepository.findBySku(oi.getId().getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));
            productOption.setQuantity(productOption.getQuantity() + oi.getQuantity());
            productOptionRepository.save(productOption);
        }

        order.setStatus(OrderStatus.CANCEL);
        String message = request.getMessage();
        orderStatusTrackingRepository.save(OrderStatusTracking.builder()
                .orderId(order.getId())
                .message(message)
                .status(request.getStatus())
                .build());

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrdersAdmin(Map<String, Object> params) {
        Pageable pageable = Utils.buildPageable(params);
        return orderRepository.findAll(pageable).getContent();
    }

    @Override
    public Order getOrderAdmin(int orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Could not find order with id " + orderId));
    }

    @Override
    @Transactional
    public Order updateOrderAdmin(UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (request.getStatus().getPriority() <= order.getStatus().getPriority()) {
            throw new UpdateOrderException("Invalid order status");
        }

        if (request.getStatus() == OrderStatus.CANCEL) {
            if (request.getMessage() == null || request.getMessage().isEmpty()) {
                throw new UpdateOrderException("You must state the reason for canceling order!");
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

        if (request.getStatus() == OrderStatus.SUCCEEDED) {
            order.getOrderItems().forEach(oi -> {
                ProductBestSeller p = productBestSellerRepository.findById(oi.getId().getSku())
                        .orElse(null);
                if (p != null) {
                    p.setSold(p.getSold() + oi.getQuantity());
                    productBestSellerRepository.save(p);
                } else {
                    productBestSellerRepository.save(ProductBestSeller.builder().sku(oi.getId().getSku()).sold(oi.getQuantity()).build());
                }
            });

        }

        return orderRepository.save(order);
    }



    @Override
    public OrderSummary summary() {
        LocalDateTime now = LocalDateTime.now();
        OrderSummary.OrderSummaryBuilder builder = OrderSummary.builder();
        builder.totalOrder(orderRepository.countByCreatedAtBetween(now.minusYears(10), now));
        builder.todayOrder(orderRepository.countByCreatedAtBetween(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0), now));
        builder.successfulOrder(orderRepository.countByStatusAndCreatedAtBetween(OrderStatus.SUCCEEDED, now.minusYears(2), now));
        builder.pendingOrder(orderRepository.countByStatusAndCreatedAtBetween(OrderStatus.WAITING, now.minusYears(2), now));
        builder.cancelledOrder(orderRepository.countByStatusAndCreatedAtBetween(OrderStatus.CANCEL, now.minusYears(2), now));
        return builder.build();
    }

    @Override
    public List<ProductBestSellerResponse> getProductBestSeller() {
        List<ProductBestSeller> productBestSellers = productBestSellerRepository.getProductBestSeller();
        List<ProductBestSellerResponse> response = new ArrayList<>();
        for (ProductBestSeller productBestSeller : productBestSellers) {
            ProductOption po = productOptionRepository.findBySku(productBestSeller.getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));
            response.add(ProductBestSellerResponse.builder()
                    .sku(po.getSku())
                    .price(po.getPrice())
                    .name(po.getProduct().getName())
                    .imageUrl(po.getProduct().getProductImages().stream().filter(i -> Objects.equals(i.getColor().getId(), po.getColor().getId())).toList().getFirst().getUrl())
                    .sold(productBestSeller.getSold())
                    .build());
        }
        return response;
    }

    @Override
    public List<TopCustomerResponse> getTopCustomer() {
        return topCustomerRepository.getTopCustomer().stream().map(TopCustomerResponse::from).toList();
    }
}
