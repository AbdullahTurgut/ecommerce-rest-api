package com.dailyalcorcode.buynowdotcom.service.user;

import com.dailyalcorcode.buynowdotcom.dtos.UserDto;
import com.dailyalcorcode.buynowdotcom.model.User;
import com.dailyalcorcode.buynowdotcom.request.CreateUserRequest;
import com.dailyalcorcode.buynowdotcom.request.UpdateUserRequest;

public interface IUserService {

    User createUser(CreateUserRequest user);

    User updateUser(Long userId, UpdateUserRequest request);

    User getUserById(Long userId);

    void deleteUser(Long userId);

    // converting
    UserDto convertToUserDto(User user);

    User getAuthenticatedUser();
}
