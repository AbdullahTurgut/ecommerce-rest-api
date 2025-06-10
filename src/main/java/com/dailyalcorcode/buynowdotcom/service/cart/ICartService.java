package com.dailyalcorcode.buynowdotcom.service.cart;

import com.dailyalcorcode.buynowdotcom.dtos.CartDto;
import com.dailyalcorcode.buynowdotcom.model.Cart;
import com.dailyalcorcode.buynowdotcom.model.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long cartId);

    Cart getCartByUserId(Long userId);

    void clearCart(Long cartId);

    Cart initializeNewCartForUser(User user);

    BigDecimal getTotalPrice(Long cartId);

    // model mapper
    CartDto convertToDto(Cart cart);
}
