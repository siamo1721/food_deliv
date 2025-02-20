package com.example.food_deliv.controller;

import com.example.food_deliv.model.Payment;
import com.example.food_deliv.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<Payment> confirmPayment(@PathVariable Long orderId) {
        Payment payment = paymentService.createPaymentIfNotExists(orderId);
        payment = paymentService.confirmPayment(orderId);
        return ResponseEntity.ok(payment);
    }
}
