package com.dailyalcorcode.buynowdotcom.service.order;

import com.dailyalcorcode.buynowdotcom.dtos.OrderDto;
import com.dailyalcorcode.buynowdotcom.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);

    List<Order> getUserOrders(Long userId);

    // Helper mapper List<Order> to List<OrderDto>
    List<OrderDto> getConvertedOrders(List<Order> orders);

    // Helper mapper order to orderDto
    OrderDto convertToOrderDto(Order order);
}
