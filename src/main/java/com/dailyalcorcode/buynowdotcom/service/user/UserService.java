package com.dailyalcorcode.buynowdotcom.service.user;

import com.dailyalcorcode.buynowdotcom.dtos.UserDto;
import com.dailyalcorcode.buynowdotcom.model.Role;
import com.dailyalcorcode.buynowdotcom.model.User;
import com.dailyalcorcode.buynowdotcom.repository.AddressRepository;
import com.dailyalcorcode.buynowdotcom.repository.RoleRepository;
import com.dailyalcorcode.buynowdotcom.repository.UserRepository;
import com.dailyalcorcode.buynowdotcom.request.CreateUserRequest;
import com.dailyalcorcode.buynowdotcom.request.UpdateUserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final AddressRepository addressRepository;

    private final RoleRepository roleRepository;

    @Override
    public User createUser(CreateUserRequest request) {
        Role userRole = Optional.ofNullable(roleRepository.findByName("ROLE_USER"))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(user.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(encoder.encode(request.getPassword()));
                    user.setRoles(Set.of(userRole)); // Collection olarak bekler
                    User savedUser = userRepository.save(user);
                    Optional.ofNullable(req.getAddressList()).ifPresent(addressList -> {
                        addressList.forEach(address -> {
                            address.setUser(savedUser);
                            addressRepository.save(address);
                        });
                    });

                    return savedUser;
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
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Log in required!"));
    }
}
