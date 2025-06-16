package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.enums.Currency;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.exception.ApiException;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;

    @Override
    public OrderResponse createOrder(UUID userId) {
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .currency(Currency.INR)
                .orderStatus(OrderStatus.PENDING)
                .build();
        //TODO: Fetch all products from cart service
        //TODO: Validate and reserve each product from inventory service
        //TODO: Fetch price for each reserved product from catalog service
        // For now inserting dummy orderItems

        OrderItem product1 = OrderItem.builder()
                .productId(UUID.randomUUID())
                .price(BigDecimal.valueOf(45.42))
                .quantity(5)
                .build();

        OrderItem product2 = OrderItem.builder()
                .productId(UUID.randomUUID())
                .price(BigDecimal.valueOf(999))
                .quantity(2)
                .build();

        product1.setTotalPrice(product1.calculateTotalPrice());
        product2.setTotalPrice(product2.calculateTotalPrice());
        order.addItem(product1);
        order.addItem(product2);

        order.setTotalPrice(order.calculateTotalPrice());
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    @Override
    public OrderResponse confirmOrder(UUID orderId, UUID userId) {
        Order order = getValidatedOrder(orderId, userId);
        if (order.getOrderStatus() != OrderStatus.PENDING)
            throw new ApiException("Can't confirm order.", HttpStatus.BAD_REQUEST);

        // TODO: Need to reduce stock of each product from inventory service
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    @Override
    public OrderResponse cancelOrder(UUID orderId, UUID userId) {
        Order order = getValidatedOrder(orderId, userId);
        if (order.getOrderStatus() != OrderStatus.CONFIRMED)
            throw new ApiException("Can't cancel order.", HttpStatus.BAD_REQUEST);

        // TODO: Need to restock each product into inventory service
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    @Override
    public OrderResponse getOrder(UUID orderId, UUID userId) {
        return OrderResponse.from(getValidatedOrder(orderId, userId));
    }

    @Override
    public List<OrderResponse> getAllOrders(UUID userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream().map(OrderResponse::from).toList();
    }

    private Order getValidatedOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException("Order not found.", HttpStatus.NOT_FOUND));

        if (!order.getUserId().equals(userId))
            throw new ApiException("Order does not belong to this user.", HttpStatus.FORBIDDEN);

        return order;
    }
}
