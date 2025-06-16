package com.dailyalcorcode.buynowdotcom.controller;

import com.dailyalcorcode.buynowdotcom.dtos.CartItemDto;
import com.dailyalcorcode.buynowdotcom.model.Cart;
import com.dailyalcorcode.buynowdotcom.model.CartItem;
import com.dailyalcorcode.buynowdotcom.model.User;
import com.dailyalcorcode.buynowdotcom.response.ApiResponse;
import com.dailyalcorcode.buynowdotcom.service.cart.ICartItemService;
import com.dailyalcorcode.buynowdotcom.service.cart.ICartService;
import com.dailyalcorcode.buynowdotcom.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            //Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        User user = userService.getAuthenticatedUser();
        Cart userCart = cartService.initializeNewCartForUser(user);
        CartItem cartItem = cartItemService.addItemToCart(userCart.getId(), productId, quantity);
        CartItemDto cartItemDto = cartItemService.covertToDto(cartItem);
        return ResponseEntity.ok(new ApiResponse("Item added successfully!", cartItemDto));
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long productId) {
        cartItemService.removeItemFromCart(cartId, productId);
        return ResponseEntity.ok(new ApiResponse("Item removed successfully!", null));
    }

    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long cartId,
                                                      @PathVariable Long productId,
                                                      @RequestParam int quantity) {
        cartItemService.updateItemQuantity(cartId, productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Item updated successfully!", null));
    }
}
