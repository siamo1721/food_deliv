package com.example.food_deliv.controller;

import com.example.food_deliv.model.Cart;
import com.example.food_deliv.request.CartItemRequest;
import com.example.food_deliv.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart newCart = cartService.createCart();
        return ResponseEntity.ok(newCart);
    }


    @PostMapping("/{cartId}/{itemType}/{itemId}")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable Long cartId,
            @PathVariable String itemType,
            @PathVariable Long itemId,
            @RequestBody CartItemRequest request) { // Используем CartItemRequest
        int quantity = request.getQuantity();
        String bread = null;
//        String drink = null;

        // Передаем bread и drink только для комплексных обедов
        if ("complex-lunches".equals(itemType)) {
            bread = request.getOptions().getBread();
//            drink = request.getOptions().getDrink();
        }
        log.info("Adding item to cart: cartId={}, itemId={}, itemType={}, quantity={}, bread={}",
                cartId, itemId, itemType, quantity, bread);
        // Передаем все данные в сервис
        Cart cart = cartService.addItemToCart(cartId, itemId, itemType, quantity, bread);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartId, itemId));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }
    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestBody CartItemRequest request) {
        log.info("Received request to update quantity for cart item {} in cart {} to {}", itemId, cartId, request.getQuantity());

        try {
            if ( request.getQuantity() < 1) {
                return ResponseEntity.badRequest().body("Invalid quantity. Must be a positive integer.");
            }

            Cart updatedCart = cartService.updateCartItemQuantity(cartId, itemId, request.getQuantity());
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            log.error("Error updating cart item quantity", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
