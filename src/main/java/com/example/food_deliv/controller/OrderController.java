package com.example.food_deliv.controller;

import com.example.food_deliv.dto.OrderDTO;
import com.example.food_deliv.model.Order;
import com.example.food_deliv.request.OrderRequest;
import com.example.food_deliv.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


        @PostMapping
        public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
            if (request.getCartId() == null) {
                return ResponseEntity.badRequest().body("Ошибка: cartId не указан");
            }

            try {
                OrderDTO order = orderService.createOrder(
                        request.getCartId(),
                        request.getFullName(),
                        request.getPhone(),
                        request.getAddress(),
                        request.getComment()
                );
                return ResponseEntity.ok(order);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при создании заказа");
            }
        }


    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
