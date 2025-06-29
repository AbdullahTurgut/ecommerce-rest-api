package com.dailyalcorcode.buynowdotcom.controller;

import com.dailyalcorcode.buynowdotcom.dtos.CartDto;
import com.dailyalcorcode.buynowdotcom.model.Cart;
import com.dailyalcorcode.buynowdotcom.response.ApiResponse;
import com.dailyalcorcode.buynowdotcom.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/user/{userId}/cart")
    public ResponseEntity<ApiResponse> getUserCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        CartDto cartDto = cartService.convertToDto(cart);
        return ResponseEntity.ok(new ApiResponse("Success", cartDto));
    }

    @DeleteMapping("/cart/{cartId}/clear")
    public void clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
    }


}
