package com.lamdangfixbug.qmshoe.payment.repository;

import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer> {
    @Query("select  pd.paymentMethod.provider, count(pd.id)  from PaymentDetails  pd group by pd.paymentMethod.provider")
    List<?> getPaymentGateWayUsageRate();
}
