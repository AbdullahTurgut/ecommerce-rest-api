package com.dailyalcorcode.buynowdotcom.service.order;

import com.dailyalcorcode.buynowdotcom.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);

    List<Order> getUserOrders(Long userId);
}
