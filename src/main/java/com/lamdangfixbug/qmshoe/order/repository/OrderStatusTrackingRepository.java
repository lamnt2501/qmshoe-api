package com.lamdangfixbug.qmshoe.order.repository;

import com.lamdangfixbug.qmshoe.order.entity.OrderStatusTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusTrackingRepository extends JpaRepository<OrderStatusTracking, Integer> {
}
