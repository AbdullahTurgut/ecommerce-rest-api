package com.dailyalcorcode.buynowdotcom.service.address;

import com.dailyalcorcode.buynowdotcom.dtos.AddressDto;
import com.dailyalcorcode.buynowdotcom.model.Address;
import com.dailyalcorcode.buynowdotcom.repository.AddressRepository;
import com.dailyalcorcode.buynowdotcom.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final IUserService userService;
    private final ModelMapper mapper;

    @Override
    public List<Address> createAddress(Long userId, List<Address> addressList) {
        return Optional.ofNullable(userService.getUserById(userId))
                .map(user -> addressList.stream().peek(address -> address.setUser(user)).toList())
                .map(addressRepository::saveAll)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address getAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.findById(addressId).ifPresentOrElse(
                addressRepository::delete, () -> {
                    throw new EntityNotFoundException("Address not found");
                }
        );
    }

    @Override
    public Address updateUserAddress(Long id, Address address) {
        return addressRepository.findById(id).map(existingAddress -> {
            existingAddress.setCountry(address.getCountry());
            existingAddress.setState(address.getState());
            existingAddress.setCity(address.getCity());
            existingAddress.setStreet(address.getStreet());
            existingAddress.setPhone(address.getPhone());
            existingAddress.setAddressType(address.getAddressType());
            return addressRepository.save(existingAddress);
        }).orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    // handling for coverting dto

    @Override
    public List<AddressDto> getConvertedAddresses(List<Address> addressList) {
        return addressList.stream().map(this::convertToDto).toList();
    }

    @Override
    public AddressDto convertToDto(Address address) {
        return mapper.map(address, AddressDto.class);
    }
}
