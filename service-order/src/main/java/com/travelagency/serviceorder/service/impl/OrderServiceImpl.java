package com.travelagency.serviceorder.service.impl;

import com.travelagency.serviceorder.dto.OrderResponseDto;
import com.travelagency.serviceorder.dto.TourDto;
import com.travelagency.serviceorder.dto.UserDto;
import com.travelagency.serviceorder.enums.OrderStatus;
import com.travelagency.serviceorder.enums.UserType;
import com.travelagency.serviceorder.model.Order;
import com.travelagency.serviceorder.repository.OrderRepository;
import com.travelagency.serviceorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final String userUrlAdress ="http://localhost:8080/user";
    private final String tourUrlAdress ="http://localhost:8081/tour";

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) throws IllegalArgumentException {
        final Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        }
        else {
            throw new IllegalArgumentException("Invalid order id");
        }
    }

    @Override
    public OrderStatus getStatusById(Long id) throws IllegalArgumentException{
        final Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            return optionalOrder.get().getOrderStatus();
        }
        else {
            throw new IllegalArgumentException("Invalid order id");
        }
    }

    private boolean checkTourForCorrectness(Long tourId) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> tourRequest = new HttpEntity<>(tourId);

        final ResponseEntity<TourDto> tourResponse = restTemplate
                .exchange(tourUrlAdress + "/" + tourId,
                        HttpMethod.GET, tourRequest, TourDto.class);

        return tourResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    private boolean checkUserForCorrectness(Long userId, UserType userType) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(userId);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/dto/" + userId,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND
                && userResponse.getBody().userType() == userType;
    }

    @Transactional
    @Override
    public Long createOrder(Long customerId, Long tourId, OrderStatus orderStatus)
            throws IllegalArgumentException {
        final Order order = new Order(customerId, tourId, orderStatus);

        if (!checkUserForCorrectness(customerId, UserType.CUSTOMER) && !checkTourForCorrectness(tourId)) {
            throw new IllegalArgumentException("Bad request");
        }
        else {
            final Order savedOrder = orderRepository.save(order);
            return savedOrder.getId();
        }

    }

    @Override
    public void updateStatusOfOrder(Long id, OrderStatus orderStatus)
            throws IllegalArgumentException {
        final Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {
            throw new IllegalArgumentException("Invalid order id");
        }

        final Order order = optionalOrder.get();

        if (orderStatus != null) order.setOrderStatus(orderStatus);

        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private String getUserNameById(long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/" +id,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getBody().firstname() + userResponse.getBody().lastname();
    }

    private String getTourNameById(long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<TourDto> userResponse = restTemplate
                .exchange(tourUrlAdress + "/" +id,
                        HttpMethod.GET, userRequest, TourDto.class);

        return userResponse.getBody().name();
    }

    private Integer getTourCostById(long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<TourDto> userResponse = restTemplate
                .exchange(tourUrlAdress + "/" +id,
                        HttpMethod.GET, userRequest, TourDto.class);

        return userResponse.getBody().cost();
    }

    private OrderResponseDto orderToResponse(Order order) {
        final OrderStatus orderStatus = order.getOrderStatus();
        final String customerName = getUserNameById(order.getCustomerId());
        final String tourName = getTourNameById(order.getTourId());
        final Integer tourCost = getTourCostById(order.getTourId());
        return new OrderResponseDto(customerName, tourName, tourCost, orderStatus);
    }

    @Override
    public List<OrderResponseDto> getAllResponse() {
        List<Order> orders = getAll();
        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();

        for(Order order: orders) {
            orderResponseDtos.add(orderToResponse(order));
        }

        return orderResponseDtos;
    }

    @Override
    public OrderResponseDto getOrderDtoById(Long id)
            throws IllegalArgumentException {
        try {
            Order order = getOrderById(id);
            return orderToResponse(order);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
