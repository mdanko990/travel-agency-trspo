package com.travelagency.serviceorder.controller;

import com.travelagency.serviceorder.dto.OrderDto;
import com.travelagency.serviceorder.dto.OrderResponseDto;
import com.travelagency.serviceorder.enums.OrderStatus;
import com.travelagency.serviceorder.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/report")
@RestController
public class OrderController {
    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        final List<OrderResponseDto> orderResponseDtos = orderService.getAllResponse();
        return ResponseEntity.ok(orderResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable long id) {
        try {
            OrderResponseDto orderResponseDto = orderService.getOrderDtoById(id);

            return ResponseEntity.ok(orderResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody OrderDto orderDto) {
        final Long customerId = orderDto.customerId();
        final Long tourId = orderDto.tourId();
        final OrderStatus orderStatus = orderDto.orderStatus();

        try {
            final long orderId = orderService.createOrder(customerId, tourId, orderStatus);
            final String orderUri = "/report/" + orderId;

            return ResponseEntity.created(URI.create(orderUri)).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> book(@PathVariable Long id, @RequestBody OrderStatus orderStatus) {
        OrderStatus newStatus = OrderStatus.NULL;
        if (orderStatus == OrderStatus.NULL) {
            newStatus = OrderStatus.BOOKED;
        }

        try {
            orderService.updateStatusOfOrder(id, newStatus);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<OrderStatus> getStatusById(@PathVariable Long id) {
        try {
            OrderResponseDto orderResponseDto = orderService.getOrderDtoById(id);

            return ResponseEntity.ok(orderResponseDto.orderStatus());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
