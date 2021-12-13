package com.travelagency.servicepayment.dto;

import com.travelagency.servicepayment.enums.OrderStatus;

public record OrderDto (String customerName, String tourName, Integer tourCost, OrderStatus orderStatus) {
}
