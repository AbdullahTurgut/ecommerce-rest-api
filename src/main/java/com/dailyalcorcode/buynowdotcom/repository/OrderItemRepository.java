package com.dailyalcorcode.buynowdotcom.repository;

import com.dailyalcorcode.buynowdotcom.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProductId(Long productId);
}
