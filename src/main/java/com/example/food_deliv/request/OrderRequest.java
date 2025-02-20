package com.example.food_deliv.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private Long cartId;
    private String fullName;
    private String phone;
    private String address;
    private String comment;
}
