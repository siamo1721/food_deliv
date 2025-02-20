package com.example.food_deliv.service;

import com.example.food_deliv.model.*;
import com.example.food_deliv.repository.CartItemRepository;
import com.example.food_deliv.repository.CartRepository;
import com.example.food_deliv.repository.ComplexLunchRepository;
import com.example.food_deliv.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ComplexLunchRepository complexlunchRepository;
    private final DishRepository dishRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Cart createCart() {
        Cart cart = new Cart();
        cart.setCreatedAt(LocalDateTime.now());
        cart.setTotalPrice(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart addItemToCart(Long cartId, Long itemId, String itemType, int quantity, String bread, String drink) {
        Cart cart = getCart(cartId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> (item.getDish() != null && item.getDish().getId().equals(itemId)) ||
                        (item.getComplexLunch() != null && item.getComplexLunch().getId().equals(itemId)))
                .findFirst();

        if (existingItem.isPresent()) {
            // Если элемент уже есть в корзине, обновляем количество
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            updateCartItemPrice(cartItem);
        } else {
            // Создаем новый элемент корзины
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);  // Устанавливаем связь с корзиной
            cartItem.setQuantity(quantity);
            cartItem.setOrderIndex(cart.getItems().size());

            if ("dishes".equals(itemType)) {
                // Обработка блюд
                Dish dish = dishRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));
                cartItem.setDish(dish);
            } else if ("complex-lunches".equals(itemType)) {
                // Обработка комплексных обедов
                ComplexLunch complexLunch = complexlunchRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Комплексный обед не найден"));
                cartItem.setComplexLunch(complexLunch);
                cartItem.setBread(bread);  // Устанавливаем хлеб
                cartItem.setDrink(drink);  // Устанавливаем напиток
            }

            updateCartItemPrice(cartItem);
            cart.addItem(cartItem);  // Добавляем элемент в корзину
        }

        updateCartTotal(cart);
        return cartRepository.save(cart);
    }
    public Cart getCart(Long cartId) {
        log.info("Attempting to get cart with id {}", cartId);

        Cart cart = cartRepository.findById(cartId)
                .orElseGet(() -> {
                    log.info("Cart not found. Creating new cart.");
                    return createCart();
                });

        // Сортируем items по orderIndex без замены коллекции
        List<CartItem> sortedItems = cart.getItems().stream()
                .sorted(Comparator.comparingInt(CartItem::getOrderIndex))
                .collect(Collectors.toList());
        cart.getItems().clear();
        cart.getItems().addAll(sortedItems);

        return cart;
    }

    @Transactional
    public Cart removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = getCart(cartId);

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар в корзине не найден"));
        cart.removeItem(itemToRemove);  // Используем метод removeItem из Cart

        // Переиндексируем оставшиеся элементы
        List<CartItem> items = cart.getItems();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOrderIndex(i);
        }

        updateCartTotal(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItemQuantity(Long cartId, Long itemId, int quantity) {
        Cart cart = getCart(cartId);

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар в корзине не найден"));

        cartItem.setQuantity(quantity);
        updateCartItemPrice(cartItem);
        updateCartTotal(cart);

        return cartRepository.save(cart);
    }

    private void updateCartItemPrice(CartItem cartItem) {
        BigDecimal unitPrice = cartItem.getDish() != null
                ? cartItem.getDish().getPrice()
                : cartItem.getComplexLunch().getPrice();
        cartItem.setPrice(unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
    }

    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }

}