package com.dailyalcorcode.buynowdotcom.dtos;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String country;
    private String state;
    private String city;
    private String street;
    private String phone;
    private String addressType;
}
