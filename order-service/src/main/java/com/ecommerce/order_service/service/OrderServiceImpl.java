package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.CartClient;
import com.ecommerce.order_service.client.InventoryClient;
import com.ecommerce.order_service.dto.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final InventoryClient inventoryClient;

    @Transactional
    @Override
    public OrderCreateResponse createOrder(UUID userId) {
        clearPendingOrders(userId);
        // Fetch all products from cart service
        ApiResponse<List<CartItemResponse>> cartResponse = cartClient.getCartItems(userId);
        if (!cartResponse.success()) {
            logger.error("Cart retrieval failed for user: {} with message: {}", userId, cartResponse.message());
            throw new ApiException("Failed to create order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (cartResponse.data() == null || cartResponse.data().isEmpty()) {
            logger.warn("No cart items found for user: {}", userId);
            throw new ApiException("Cart is empty.", HttpStatus.BAD_REQUEST);
        }

        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .currency(Currency.INR)
                .orderStatus(OrderStatus.PENDING)
                .build();

        // Validate and reserve each product from inventory service
        ApiResponse<List<ReserveStockItemResponse>> reservationRes =
                inventoryClient.reserveStocks(ReserveStockRequest.from(orderId, cartResponse.data()));
        if (!reservationRes.success() || reservationRes.data() == null || reservationRes.data().isEmpty()) {
            logger.error("Product reservation failed for user: {} with message: {}", userId, reservationRes.message());
            throw new ApiException("Failed to reserve products.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<OrderItemCreateResponse> orderItemResponses = new ArrayList<>();
        //TODO: Fetch price for each reserved product from catalog service
        for (ReserveStockItemResponse reserveStockItemResponse : reservationRes.data()) {
            BigDecimal price = BigDecimal.valueOf(100.99);
            OrderItem orderItem = OrderItem.builder()
                    .productId(reserveStockItemResponse.productId())
                    .price(price)
                    .quantity(reserveStockItemResponse.reservedQuantity())
                    .build();

            orderItem.setTotalPrice(orderItem.calculateTotalPrice());
            order.addItem(orderItem);

            orderItemResponses.add(OrderItemCreateResponse.from(reserveStockItemResponse, price, orderItem.getTotalPrice()));
        }

        order.setTotalPrice(order.calculateTotalPrice());
        orderRepository.save(order);

        return new OrderCreateResponse(orderId, orderItemResponses, order.getTotalPrice(), order.getCurrency(), OrderStatus.PENDING);
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

    private void clearPendingOrders(UUID userId) {
        List<Order> pendingOrders = orderRepository.findAllByUserIdAndOrderStatus(userId, OrderStatus.PENDING);

        for (Order order : pendingOrders) {
            // TODO: Need to send event or REST call to inventory to release stocks
            orderRepository.delete(order);
        }
    }
}
