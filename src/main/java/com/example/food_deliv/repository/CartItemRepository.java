package com.example.food_deliv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.food_deliv.model.CartItem;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Метод для поиска CartItem по ID
    Optional<CartItem> findById(Long id);
}
