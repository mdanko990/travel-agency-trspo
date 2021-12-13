package com.travelagency.servicepayment.service;

import com.travelagency.servicepayment.dto.PaymentResponseDto;
import com.travelagency.servicepayment.enums.PaymentMethod;
import com.travelagency.servicepayment.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    List<Payment> getAll();
    Payment getPaymentById(Long id) throws IllegalArgumentException;
    long createPayment(Long customerId, Long orderId, PaymentMethod method) throws IllegalArgumentException;
    void updatePayment(Long id, Long customerId, Long orderId, PaymentMethod method) throws IllegalArgumentException;
    void deletePayment(Long id);
    Integer getIncomes();
    List<PaymentResponseDto> getAllResponse();
    PaymentResponseDto getPaymentResponseDto(Long id) throws IllegalArgumentException;
}
