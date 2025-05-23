package com.dailyalcorcode.buynowdotcom.repository;

import com.dailyalcorcode.buynowdotcom.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
