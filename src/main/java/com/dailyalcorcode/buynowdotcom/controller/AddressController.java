package com.dailyalcorcode.buynowdotcom.controller;

import com.dailyalcorcode.buynowdotcom.dtos.AddressDto;
import com.dailyalcorcode.buynowdotcom.model.Address;
import com.dailyalcorcode.buynowdotcom.response.ApiResponse;
import com.dailyalcorcode.buynowdotcom.service.address.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/addresses")
public class AddressController {

    private final IAddressService addressService;

    @PostMapping("/{userId}/create")
    public ResponseEntity<ApiResponse> createAddress(@PathVariable Long userId, @RequestBody List<Address> addressList) {
        List<Address> addresses = addressService.createAddress(userId, addressList);
        List<AddressDto> addressDtoList = addressService.getConvertedAddresses(addresses);
        return ResponseEntity.ok(new ApiResponse("Address created successfully", addressDtoList));
    }

    @GetMapping("/address/user/{userId}")
    public ResponseEntity<ApiResponse> getUserAddresses(@PathVariable Long userId) {
        List<Address> addresses = addressService.getUserAddresses(userId);
        List<AddressDto> addressDtoList = addressService.getConvertedAddresses(addresses);
        return ResponseEntity.ok(new ApiResponse("Success", addressDtoList));
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long addressId) {
        Address address = addressService.getAddressById(addressId);
        AddressDto addressDto = addressService.convertToDto(address);
        return ResponseEntity.ok(new ApiResponse("Success", addressDto));
    }


    @DeleteMapping("/address/{addressId}/delete")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(new ApiResponse("Address deleted successfully", addressId));
    }

    @PutMapping("/address/{id}/update")
    public ResponseEntity<ApiResponse> updateUserAddress(@PathVariable Long id, @RequestBody Address address) {
        Address theAddress = addressService.updateUserAddress(id, address);
        AddressDto addressDto = addressService.convertToDto(theAddress);
        return ResponseEntity.ok(new ApiResponse("Address updated successfully", addressDto));
    }
}
