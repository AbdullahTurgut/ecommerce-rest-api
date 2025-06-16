package com.dailyalcorcode.buynowdotcom.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private List<AddressDto> addressList;
    private CartDto cart;
}
