package com.travelagency.servicepayment.dto;

import com.travelagency.servicepayment.enums.PaymentMethod;

public record PaymentResponseDto(String customerName, Integer orderCost, PaymentMethod method) {
}
