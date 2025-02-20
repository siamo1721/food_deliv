// PaymentService.java
package com.example.food_deliv.service;

import com.example.food_deliv.model.Order;
import com.example.food_deliv.model.OrderStatus;
import com.example.food_deliv.model.Payment;
import com.example.food_deliv.model.PaymentStatus;
import com.example.food_deliv.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;


    @Transactional
    public Payment createPaymentIfNotExists(Long orderId) {
        Payment existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment != null) {
            return existingPayment;
        }

        Order order = orderService.getOrderById(orderId);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment confirmPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            throw new RuntimeException("Платёж не найден");
        }

        if (payment.getStatus() == PaymentStatus.CONFIRMED) {
            return payment;
        }

        payment.setStatus(PaymentStatus.CONFIRMED);
        payment.setConfirmedAt(LocalDateTime.now());

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);

        return paymentRepository.save(payment);
    }
}
