package com.dailyalcorcode.buynowdotcom.service.order;

import com.dailyalcorcode.buynowdotcom.dtos.OrderDto;
import com.dailyalcorcode.buynowdotcom.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);


    // Helper mapper List<Order> to List<OrderDto>
    List<OrderDto> getUserOrders(Long userId);

    // Helper mapper order to orderDto
    OrderDto convertToOrderDto(Order order);
}
