package com.travelagency.servicepayment.dto;

import com.travelagency.servicepayment.enums.PaymentMethod;

public record PaymentDto(Long customerId, Long orderId, PaymentMethod method) {
}
