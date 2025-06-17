package com.dailyalcorcode.buynowdotcom.service.order;

import com.dailyalcorcode.buynowdotcom.dtos.OrderDto;
import com.dailyalcorcode.buynowdotcom.enums.OrderStatus;
import com.dailyalcorcode.buynowdotcom.model.Cart;
import com.dailyalcorcode.buynowdotcom.model.Order;
import com.dailyalcorcode.buynowdotcom.model.OrderItem;
import com.dailyalcorcode.buynowdotcom.model.Product;
import com.dailyalcorcode.buynowdotcom.repository.OrderRepository;
import com.dailyalcorcode.buynowdotcom.repository.ProductRepository;
import com.dailyalcorcode.buynowdotcom.request.PaymentRequest;
import com.dailyalcorcode.buynowdotcom.service.cart.ICartService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    // helper methods for placeOrder

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getUnitPrice(),
                            cartItem.getQuantity()
                    );
                }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // payment intent
    @Override
    public String createPaymentIntent(PaymentRequest request) throws StripeException {
        long amountInSmallestUnit = Math.round(request.getAmount() * 100); // expecting "cents" so multiply 100
        PaymentIntent intent = PaymentIntent.create(
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInSmallestUnit)
                        .setCurrency(request.getCurrency())
                        .addPaymentMethodType("card")
                        .build()
        );
        return intent.getClientSecret();
    }


    // Helper mapper List<Order> to List<OrderDto>
    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToOrderDto).toList();
    }

    // Helper mapper order to orderDto
    @Override
    public OrderDto convertToOrderDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
