package com.travelagency.servicepayment.repository;

import com.travelagency.servicepayment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
