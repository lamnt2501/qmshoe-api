package com.lamdangfixbug.qmshoe.payment.repository;

import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.stream.Stream;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer> {
    @Query("select  pd.paymentMethod.provider, count(pd.id)  from PaymentDetails  pd group by pd.paymentMethod.provider")
    List<?> getPaymentGateWayUsageRate();

    @Query("select month(pd.createdAt),sum(pd.amount) from PaymentDetails  pd  where year(pd.createdAt) = :year and pd.status = 'PAID' group by month(pd.createdAt)")
    List<?> getRevenue(int year);

//    List<?> getRevenue(int year,int month);
//
//    List<?> getRevenue(int year, int month, int day);
}
