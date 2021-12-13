package com.travelagency.servicepayment.service.impl;

import com.travelagency.servicepayment.dto.OrderDto;
import com.travelagency.servicepayment.dto.PaymentResponseDto;
import com.travelagency.servicepayment.dto.UserDto;
import com.travelagency.servicepayment.enums.OrderStatus;
import com.travelagency.servicepayment.enums.PaymentMethod;
import com.travelagency.servicepayment.model.Payment;
import com.travelagency.servicepayment.repository.PaymentRepository;
import com.travelagency.servicepayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final String userUrlAdress ="http://localhost:8080/user";
    private final String orderUrlAdress ="http://localhost:8082/order";

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Long id) throws IllegalArgumentException {
        final Optional<Payment> optionalPayment = paymentRepository.findById(id);

        if (optionalPayment.isPresent()) {
            return optionalPayment.get();
        }
        else {
            throw new IllegalArgumentException("Invalid payment id");
        }
    }

    private boolean checkUserForCorrectness(Long userId) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(userId);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/dto/" + userId,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    private boolean checkOrderForCorrectness(Long orderId) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> orderRequest = new HttpEntity<>(orderId);

        final ResponseEntity<OrderDto> orderResponse = restTemplate
                .exchange(orderUrlAdress + "/" + orderId,
                        HttpMethod.GET, orderRequest, OrderDto.class);

        return orderResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    @Transactional
    @Override
    public long createPayment(Long customerId, Long orderId, PaymentMethod method)
            throws IllegalArgumentException {
        final Payment payment = new Payment(customerId, orderId, method);

        if (!checkUserForCorrectness(customerId)
                || !checkOrderForCorrectness(orderId)) {
            throw new IllegalArgumentException("Bad request");
        }
        else {
            final Payment savedPayment = paymentRepository.save(payment);

            return savedPayment.getId();
        }
    }

    @Transactional
    @Override
    public void updatePayment(Long id, Long customerId, Long orderId, PaymentMethod method)
            throws IllegalArgumentException {
        final Optional<Payment> optionalPayment = paymentRepository.findById(id);

        if (optionalPayment.isEmpty()) {
            throw new IllegalArgumentException("Invalid payment id");
        }

        final Payment payment = optionalPayment.get();

        if (customerId != null && customerId != -1) {
            if (!checkUserForCorrectness(customerId)) {
                throw new IllegalArgumentException("Invalid customer id");
            }

            payment.setCustomerId(customerId);
        }
        if (orderId != null && orderId != -1) {
            if (!checkOrderForCorrectness(orderId)) {
                throw new IllegalArgumentException("Invalid order id");
            }

            payment.setOrderId(orderId);
        }

        if(method != null && method != PaymentMethod.NULL){
            payment.setMethod(method);
        }

        paymentRepository.save(payment);

    }

    @Transactional
    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    private String getNameById(Long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/" +id,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getBody().name();
    }

    private Integer getCostById(Long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> orderRequest = new HttpEntity<>(id);

        final ResponseEntity<OrderDto> orderResponse = restTemplate
                .exchange(orderUrlAdress + "/" +id,
                        HttpMethod.GET, orderRequest, OrderDto.class);

        return orderResponse.getBody().tourCost();
    }

    private OrderStatus getStatusById(Long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> orderRequest = new HttpEntity<>(id);

        final ResponseEntity<OrderDto> orderResponse = restTemplate
                .exchange(orderUrlAdress + "/" +id,
                        HttpMethod.GET, orderRequest, OrderDto.class);

        return orderResponse.getBody().orderStatus();
    }

    private PaymentResponseDto paymentToResponse(Payment payment) {
        final String customerName = getNameById(payment.getCustomerId());
        final Integer orderCost = getCostById(payment.getOrderId());
        final PaymentMethod method = payment.getMethod();

        return new PaymentResponseDto(customerName, orderCost, method);
    }

    @Override
    public Integer getIncomes() {
        List<Payment> payments = getAll();
        Integer cost = 0;
        for(Payment payment: payments) {
            cost += paymentToResponse(payment).orderCost();
        }

        return cost;
    }

    @Override
    public List<PaymentResponseDto> getAllResponse() {
        List<Payment> payments = getAll();
        List<PaymentResponseDto> paymentResponseDtos = new ArrayList<>();

        for(Payment payment: payments) {
            paymentResponseDtos.add(paymentToResponse(payment));
        }

        return paymentResponseDtos;
    }

    @Override
    public PaymentResponseDto getPaymentResponseDto(Long id)
            throws IllegalArgumentException {
        try {
            Payment payment = getPaymentById(id);
            return paymentToResponse(payment);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
