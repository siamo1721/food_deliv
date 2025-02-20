package com.example.food_deliv.repository;

import com.example.food_deliv.model.Dish;
import com.example.food_deliv.model.DishCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCategory(DishCategory category);
}
