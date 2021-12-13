package com.travelagency.serviceorder.dto;

import com.travelagency.serviceorder.enums.OrderStatus;

public record OrderDto(Long customerId, Long tourId, OrderStatus orderStatus) {
}
