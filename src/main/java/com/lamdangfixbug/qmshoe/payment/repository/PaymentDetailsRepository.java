package com.lamdangfixbug.qmshoe.payment.repository;

import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer> {

}
