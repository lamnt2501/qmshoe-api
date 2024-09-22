package com.lamdangfixbug.qmshoe.order.service;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.entity.TopCustomer;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.payload.response.OrderResponse;
import com.lamdangfixbug.qmshoe.order.payload.response.OrderSummary;
import com.lamdangfixbug.qmshoe.order.payload.response.ProductBestSellerResponse;
import com.lamdangfixbug.qmshoe.order.payload.response.TopCustomerResponse;


import java.util.List;
import java.util.Map;

public interface OrderService {
    boolean orderExists(int orderId);

    Order placeOrder(OrderRequest orderRequest);

    Order getOrder(int orderId);

    List<Order> getAllOrders(Map<String, Object> params);

    Order updateOrder(UpdateOrderStatusRequest request);

    Order getOrderAdmin(int orderId);

    List<Order> getAllOrdersAdmin(Map<String, Object> params);

    Order updateOrderAdmin(UpdateOrderStatusRequest request);

    OrderSummary summary();

    List<ProductBestSellerResponse> getProductBestSeller();

    List<TopCustomerResponse> getTopCustomer();

    List<OrderResponse> getOrdersByCustomerId(int customerId);

    int countOrderByOrderStatus(OrderStatus status);
}
