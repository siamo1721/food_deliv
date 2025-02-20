package com.example.food_deliv.service;


import com.example.food_deliv.model.Cart;
import com.example.food_deliv.model.Order;
import com.example.food_deliv.model.OrderStatus;
import com.example.food_deliv.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final TelegramService telegramService;


    public Order createOrder(Long cartId, String fullName, String phone, String address, String comment) {
        Cart cart = cartService.getCart(cartId);
        if (cart == null) {
            throw new EntityNotFoundException("Корзина не найдена");
        }

        Order order = new Order();
        order.setCart(cart);
        order.setFullName(fullName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setComment(comment);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        Order savedOrder = orderRepository.save(order);

        telegramService.sendOrderNotification(savedOrder);



        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
    }
}