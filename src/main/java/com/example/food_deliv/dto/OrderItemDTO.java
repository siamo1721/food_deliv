package com.example.food_deliv.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter

public class OrderItemDTO {
    private Long id;
    private Long dishId;
    private Long complexLunchId;
    private Integer quantity;
    private BigDecimal price;
    private String bread;
    private String drink;

    // Геттеры и сеттеры
}
