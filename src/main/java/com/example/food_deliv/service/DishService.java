package com.example.food_deliv.service;

import com.example.food_deliv.model.Dish;
import com.example.food_deliv.model.DishCategory;
import com.example.food_deliv.repository.DishRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class DishService {
    private final DishRepository dishRepository;


    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public List<Dish> getDishesByCategory(DishCategory category) {
        return dishRepository.findByCategory(category);
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));
    }
}
