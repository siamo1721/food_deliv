package com.example.food_deliv.service;

import com.example.food_deliv.config.OrderCreatedEvent;
import com.example.food_deliv.dto.OrderDTO;
import com.example.food_deliv.dto.OrderItemDTO;
import com.example.food_deliv.model.*;
import com.example.food_deliv.repository.OrderItemRepository;
import com.example.food_deliv.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderItemRepository orderItemRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderDTO createOrder(Long cartId, String fullName, String phone, String address, String comment) {
        try {
            log.info("Создание заказа для корзины: {}", cartId);
            Cart cart = cartService.getCart(cartId);
            if (cart == null) {
                throw new EntityNotFoundException("Корзина не найдена");
            }

            // Создаем заказ
            Order order = new Order();
            order.setFullName(fullName);
            order.setPhone(phone);
            order.setAddress(address);
            order.setComment(comment);
            order.setStatus(OrderStatus.NEW);
            order.setCreatedAt(LocalDateTime.now());
            order.setTotalAmount(cart.getTotalPrice());

            // Переносим товары из корзины в заказ
            List<OrderItem> orderItems = cart.getItems().stream()
                    .map(cartItem -> {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order);
                        orderItem.setDish(cartItem.getDish());
                        orderItem.setComplexLunch(cartItem.getComplexLunch());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setPrice(cartItem.getPrice());
                        orderItem.setBread(cartItem.getBread());
//                        orderItem.setDrink(cartItem.getDrink());
                        return orderItem;
                    })
                    .collect(Collectors.toList());

            order.setItems(orderItems);

            // Сохраняем заказ
            Order savedOrder = orderRepository.save(order);
            log.info("Заказ успешно создан: {}", savedOrder.getId());

            // Публикуем событие о создании заказа
            eventPublisher.publishEvent(new OrderCreatedEvent(savedOrder));

            // Очищаем корзину
            cartService.clearCart(cartId);
            log.info("Корзина успешно очищена: {}", cartId);

            // Преобразуем сущность в DTO
            return convertToDTO(savedOrder);
        } catch (Exception e) {
            log.error("Ошибка при создании заказа: ", e);
            throw e;
        }
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setFullName(order.getFullName());
        orderDTO.setPhone(order.getPhone());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setComment(order.getComment());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setTotalAmount(order.getTotalAmount());

        List<OrderItemDTO> orderItemDTOs = order.getItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        orderDTO.setItems(orderItemDTOs);

        return orderDTO;
    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setDishId(orderItem.getDish() != null ? orderItem.getDish().getId() : null);
        orderItemDTO.setComplexLunchId(orderItem.getComplexLunch() != null ? orderItem.getComplexLunch().getId() : null);
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setBread(orderItem.getBread());
//        orderItemDTO.setDrink(orderItem.getDrink());

        return orderItemDTO;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
    }
}