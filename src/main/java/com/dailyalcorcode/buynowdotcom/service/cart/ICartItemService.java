package com.dailyalcorcode.buynowdotcom.service.cart;

import com.dailyalcorcode.buynowdotcom.dtos.CartItemDto;
import com.dailyalcorcode.buynowdotcom.model.CartItem;

public interface ICartItemService {

    CartItem addItemToCart(Long cartId, Long productId, int quantity);

    void removeItemFromCart(Long cartId, Long productId);

    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);

    // model mapper area
    CartItemDto covertToDto(CartItem cartItem);
}
