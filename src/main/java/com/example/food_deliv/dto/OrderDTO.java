package com.example.food_deliv.dto;

import com.example.food_deliv.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter

@Setter
public class OrderDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String address;
    private String comment;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;

    // Геттеры и сеттеры
}

