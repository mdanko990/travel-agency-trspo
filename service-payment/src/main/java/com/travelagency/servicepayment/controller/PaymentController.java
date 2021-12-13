package com.travelagency.servicepayment.controller;

import com.travelagency.servicepayment.dto.PaymentDto;
import com.travelagency.servicepayment.dto.PaymentResponseDto;
import com.travelagency.servicepayment.enums.PaymentMethod;
import com.travelagency.servicepayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/payment")
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAll() {
        final List<PaymentResponseDto> paymentResponseDtos = paymentService.getAllResponse();
        return ResponseEntity.ok(paymentResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getById(@PathVariable long id) {
        try {
            PaymentResponseDto taxReportResponseDto = paymentService.getPaymentResponseDto(id);

            return ResponseEntity.ok(taxReportResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody PaymentDto paymentDto) {
        final Long customerId = paymentDto.customerId();
        final Long orderId = paymentDto.orderId();
        final PaymentMethod method = paymentDto.method();

        try {
            final long taxReportId = paymentService
                    .createPayment(customerId, orderId, method);
            final String paymentUri = "/payment/" + taxReportId;

            return ResponseEntity.created(URI.create(paymentUri)).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> change(@PathVariable long id,
                                       @RequestBody PaymentDto paymentDto) {
        final PaymentMethod method = paymentDto.method();
        long customerId;
        if (paymentDto.customerId() == null) {
            customerId = -1;
        }
        else {
            customerId = paymentDto.customerId();
        }

        long orderId;
        if (paymentDto.orderId() == null) {
            orderId = -1;
        }
        else {
            orderId = paymentDto.orderId();
        }

        try {
            paymentService.updatePayment(id, customerId, orderId, method);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        paymentService.deletePayment(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/incomes")
    public ResponseEntity<Integer> countIncomes(){
        final Integer result = paymentService.getIncomes();
        return ResponseEntity.ok(result);
    }
}
