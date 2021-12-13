package com.travelagency.serviceorder.dto;


import com.travelagency.serviceorder.enums.OrderStatus;

public record OrderResponseDto(String customerName, String tourName, Integer tourCost, OrderStatus orderStatus) {

}
