package com.example.food_deliv.service;

import com.example.food_deliv.config.OrderCreatedEvent;
import com.example.food_deliv.model.*;
import com.example.food_deliv.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final TelegramService telegramService;
    private final CartService cartService;
    private final OrderService orderService;

    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        Order order = event.getOrder();
        createPaymentIfNotExists(order.getId());
    }



    @Transactional
    public Payment createPaymentIfNotExists(Long orderId) {
        Payment existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment != null) {
            logger.info("Платёж уже существует для заказа {}", orderId);
            return existingPayment;
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            logger.error("Заказ не найден: {}", orderId);
            throw new RuntimeException("Заказ не найден");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        logger.info("Создан новый платёж для заказа {}", orderId);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment confirmPayment(Long orderId) {
        // Создаем платёж, если он не существует
        Payment payment = createPaymentIfNotExists(orderId);

        if (payment == null) {
            throw new RuntimeException("Платёж не найден");
        }

        if (payment.getStatus() == PaymentStatus.CONFIRMED) {
            return payment;
        }

        payment.setStatus(PaymentStatus.CONFIRMED);
        payment.setConfirmedAt(LocalDateTime.now());

        Order order = payment.getOrder();
        if (order == null) {
            throw new RuntimeException("Заказ не найден");
        }

        order.setStatus(OrderStatus.PAID);

        try {
            telegramService.sendOrderNotification(order);
        } catch (Exception e) {
            logger.error("Ошибка при отправке уведомления в Telegram: {}", e.getMessage());
        }

        return paymentRepository.save(payment);
    }
}