package com.example.food_deliv.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
@Getter
@Setter
@Data
@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_seq")
    @SequenceGenerator(name = "dish_seq", sequenceName = "dish_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String description;
    private Integer weight;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private DishCategory category;

    private String imageUrl;

}