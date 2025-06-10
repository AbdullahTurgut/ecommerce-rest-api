package com.dailyalcorcode.buynowdotcom.service.address;

import com.dailyalcorcode.buynowdotcom.dtos.AddressDto;
import com.dailyalcorcode.buynowdotcom.model.Address;

import java.util.List;

public interface IAddressService {

    List<Address> createAddress(List<Address> addressList);

    List<Address> getUserAddresses(Long userId);

    Address getAddressById(Long addressId);

    void deleteAddress(Long addressId);

    Address updateUserAddress(Long id, Address address);

    List<AddressDto> getConvertedAddresses(List<Address> addresses);

    AddressDto convertToDto(Address address);
}
