package com.dailyalcorcode.buynowdotcom.service.user;

import com.dailyalcorcode.buynowdotcom.dtos.CartDto;
import com.dailyalcorcode.buynowdotcom.dtos.OrderDto;
import com.dailyalcorcode.buynowdotcom.dtos.UserDto;
import com.dailyalcorcode.buynowdotcom.model.User;
import com.dailyalcorcode.buynowdotcom.repository.CartItemRepository;
import com.dailyalcorcode.buynowdotcom.repository.UserRepository;
import com.dailyalcorcode.buynowdotcom.request.CreateUserRequest;
import com.dailyalcorcode.buynowdotcom.request.UpdateUserRequest;
import com.dailyalcorcode.buynowdotcom.service.cart.ICartService;
import com.dailyalcorcode.buynowdotcom.service.order.IOrderService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CartItemRepository cartItemRepository;
    private final IOrderService orderService;
    private final ICartService cartService;


    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(user.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new EntityExistsException(request.getEmail() + " already exists!"));
    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest request) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new EntityNotFoundException("User Not Found!"));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new EntityNotFoundException("User not found");
        });
    }


    // converting
    @Override
    public UserDto convertToUserDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        CartDto cartDto = cartService.convertToDto(user.getCart());
        List<OrderDto> orderDtoList = orderService.getUserOrders(user.getId());
        userDto.setOrders(orderDtoList);
        userDto.setCart(cartDto);
        return userDto;
    }
}
