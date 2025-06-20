package com.dailyalcorcode.buynowdotcom.controller;

import com.dailyalcorcode.buynowdotcom.dtos.OrderDto;
import com.dailyalcorcode.buynowdotcom.model.Order;
import com.dailyalcorcode.buynowdotcom.request.PaymentRequest;
import com.dailyalcorcode.buynowdotcom.response.ApiResponse;
import com.dailyalcorcode.buynowdotcom.service.order.IOrderService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/user/{userId}/place-order")
    public ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto = orderService.convertToOrderDto(order);
        return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDto));
    }

    @GetMapping("/user/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        List<OrderDto> orderDtoList = orderService.getUserOrders(userId);
        return ResponseEntity.ok(new ApiResponse("Success!", orderDtoList));
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) throws StripeException {
        String clientSecret = orderService.createPaymentIntent(request);
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }
}
