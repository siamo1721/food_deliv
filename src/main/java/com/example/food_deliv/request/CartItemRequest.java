package com.example.food_deliv.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private int quantity;
    private Options options;
    @Getter
    @Setter
    public static class Options {
        private String bread;
        private String drink;
    }
}
