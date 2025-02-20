package com.example.food_deliv.controller;

import com.example.food_deliv.model.Dish;
import com.example.food_deliv.model.DishCategory;
import com.example.food_deliv.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Dish>> getDishesByCategory(@PathVariable DishCategory category) {
        return ResponseEntity.ok(dishService.getDishesByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }
}
