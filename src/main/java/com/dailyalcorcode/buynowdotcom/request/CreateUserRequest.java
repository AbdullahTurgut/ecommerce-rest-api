package com.dailyalcorcode.buynowdotcom.request;

import com.dailyalcorcode.buynowdotcom.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Address> addresses;
}
