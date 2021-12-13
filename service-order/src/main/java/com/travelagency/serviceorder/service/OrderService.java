package com.travelagency.serviceorder.service;

import com.travelagency.serviceorder.dto.OrderResponseDto;
import com.travelagency.serviceorder.enums.OrderStatus;
import com.travelagency.serviceorder.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAll();
    Order getOrderById(Long id) throws IllegalArgumentException;
    OrderStatus getStatusById(Long id) throws IllegalArgumentException;
    Long createOrder(Long customerId, Long tourId, OrderStatus orderStatus) throws IllegalArgumentException;
    void updateStatusOfOrder(Long id, OrderStatus orderStatus) throws IllegalArgumentException;
    void deleteOrder(Long id);
    List<OrderResponseDto> getAllResponse();
    OrderResponseDto getOrderDtoById(Long id) throws IllegalArgumentException;
}
