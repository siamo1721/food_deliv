package com.example.food_deliv.model;

import lombok.Data;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Data
@Entity
@Table(name = "complex_lunches")
public class ComplexLunch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "complex_lunch_seq")
    @SequenceGenerator(name = "complex_lunch_seq", sequenceName = "complex_lunch_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "complex_lunch_dishes",
            joinColumns = @JoinColumn(name = "complex_lunch_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private Set<Dish> dishes = new HashSet<>();


}
