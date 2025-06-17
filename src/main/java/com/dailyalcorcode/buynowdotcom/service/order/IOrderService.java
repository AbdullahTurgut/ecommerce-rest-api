package com.dailyalcorcode.buynowdotcom.service.order;

import com.dailyalcorcode.buynowdotcom.dtos.OrderDto;
import com.dailyalcorcode.buynowdotcom.model.Order;
import com.dailyalcorcode.buynowdotcom.request.PaymentRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);


    // payment intent
    String createPaymentIntent(PaymentRequest request) throws StripeException;

    // Helper mapper List<Order> to List<OrderDto>
    List<OrderDto> getUserOrders(Long userId);

    // Helper mapper order to orderDto
    OrderDto convertToOrderDto(Order order);
}
