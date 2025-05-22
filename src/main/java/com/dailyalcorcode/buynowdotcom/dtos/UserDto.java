package com.dailyalcorcode.buynowdotcom.dtos;

import com.dailyalcorcode.buynowdotcom.model.Cart;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Cart cart;
    private List<OrderDto> orders;
}
