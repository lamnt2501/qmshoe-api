package com.lamdangfixbug.qmshoe.order.service;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order placeOrder(OrderRequest orderRequest);
    Order getOrder(int orderId);
    List<Order> getAllOrders(Map<String,Object> params);
    Order updateOrder(UpdateOrderStatusRequest request);
    Order getOrderAdmin(int orderId);
    List<Order> getAllOrdersAdmin(Map<String,Object> params);
    Order updateOrderAdmin(UpdateOrderStatusRequest request);
}
