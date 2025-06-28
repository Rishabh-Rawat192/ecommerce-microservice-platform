package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.CartClient;
import com.ecommerce.order_service.client.CatalogClient;
import com.ecommerce.order_service.client.InventoryClient;
import com.ecommerce.order_service.dto.*;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.enums.Currency;
import com.ecommerce.order_service.enums.OrderItemStatus;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.enums.ReservationItemStatus;
import com.ecommerce.order_service.exception.ApiException;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final InventoryClient inventoryClient;
    private final CatalogClient catalogClient;
    private final OrderProducerService orderProducerService;

    @Transactional
    @Override
    public OrderCreateResponse createOrder(UUID userId) {
        List<CartItemResponse> cartItems = fetchValidCartItems(userId);
        Map<UUID, BigDecimal> productToPrice = fetchValidProductPrices(cartItems);
        List<CartItemWithPrice> cartItemWithPrices = buildCartItemsWithPrice(cartItems, productToPrice);

        UUID orderId = UUID.randomUUID();
        Map<UUID, ReserveStockItemResponse> productToReservation = reserveStocks(orderId, cartItemWithPrices);
        registerCreateOrderTransactionHooks(orderId, userId);

        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .currency(Currency.INR)
                .orderStatus(OrderStatus.PENDING)
                .build();

        List<OrderItemCreateResponse> orderItemCreateResponses = addItemsToOrderAndBuildResponse(cartItems, productToPrice,
                productToReservation, order);

        order.setTotalPrice(order.calculateTotalPrice());
        orderRepository.save(order);

        return new OrderCreateResponse(orderId, orderItemCreateResponses, order.getTotalPrice(), order.getCurrency(), OrderStatus.PENDING);
    }

    @Transactional
    @Override
    public OrderResponse confirmOrder(UUID orderId, UUID userId) {
        Order order = getValidatedOrder(orderId, userId);
        if (order.getOrderStatus() != OrderStatus.PENDING)
            throw new ApiException("Can't confirm order.", HttpStatus.BAD_REQUEST);

        // Reduce stock of each product from inventory service
        confirmReservation(orderId);
        registerConfirmOrderTransactionHook(orderId, userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    @Transactional
    @Override
    public OrderResponse cancelOrder(UUID orderId, UUID userId) {
        Order order = getValidatedOrder(orderId, userId);
        if (order.getOrderStatus() != OrderStatus.CONFIRMED)
            throw new ApiException("Can't cancel order.", HttpStatus.BAD_REQUEST);

        registerCancelOrderTransactionHook(orderId, userId);
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    @Transactional
    @Override
    public void expireStaleOrders(LocalDateTime expiryThreshold) {
        List<Order> staleOrders = orderRepository.findAllByOrderStatusAndCreatedAtBefore(OrderStatus.PENDING, expiryThreshold);

        List <OrderExpiredEvent> expiredEvents = new ArrayList<>();
        for (Order order : staleOrders) {
            try {
                UUID orderId = order.getId();
                UUID userId = order.getUserId();

                logger.info("Expiring order: {}", orderId);
                order.setOrderStatus(OrderStatus.EXPIRED);
                orderRepository.save(order);

                expiredEvents.add(new OrderExpiredEvent(orderId, userId));
            } catch (Exception e) {
                logger.error("Failed to expire order: {}. Error: {}", order.getId(), e.getMessage());
            }
        }

        registerExpireOrdersTransactionHook(expiredEvents);
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

    private List<CartItemResponse> fetchValidCartItems(UUID userId) {
        ApiResponse<List<CartItemResponse>> cartResponse = cartClient.getCartItems(userId);
        if (cartResponse == null || !cartResponse.success()) {
            logger.error("Cart retrieval failed for user: {} with message: {}",
                    userId, cartResponse != null ? cartResponse.message() : "No response received.");
            throw new ApiException("Failed to create order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<CartItemResponse> cartItems = cartResponse.data();
        if (cartItems == null || cartItems.isEmpty()) {
            logger.warn("No cart items found for user: {}", userId);
            throw new ApiException("Cart is empty.", HttpStatus.BAD_REQUEST);
        }

        return cartItems;
    }

    private Map<UUID, BigDecimal> fetchValidProductPrices(List<CartItemResponse> cartItems) {
        ApiResponse<List<ProductPriceResponse>> priceRes = catalogClient.getProductsPrice(ProductsPriceRequest.from(cartItems));
        if (priceRes == null || !priceRes.success() || priceRes.data() == null || priceRes.data().isEmpty()) {
            logger.error("Product price fetching failed with message: {}",
                    priceRes != null ? priceRes.message() : "No response received");
            throw new ApiException("Failed to fetch product prices.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return priceRes.data().stream()
                .filter(ProductPriceResponse::found)
                .collect(Collectors.toMap(ProductPriceResponse::productId, ProductPriceResponse::price));
    }

    private List<CartItemWithPrice> buildCartItemsWithPrice(List<CartItemResponse> cartItems, Map<UUID, BigDecimal> productToPrice) {
        return cartItems.stream()
                .filter(item -> productToPrice.containsKey(item.productId()))
                .map(item -> CartItemWithPrice.from(item, productToPrice.get(item.productId())))
                .toList();
    }

    private Map<UUID, ReserveStockItemResponse> reserveStocks(UUID orderId, List<CartItemWithPrice> cartItemWithPrices) {
        ReserveStockRequest reserveStockRequest = ReserveStockRequest.from(orderId, cartItemWithPrices);
        ApiResponse<List<ReserveStockItemResponse>> reservationRes = inventoryClient.createReservation(reserveStockRequest);

        if (reservationRes == null || !reservationRes.success() || reservationRes.data() == null || reservationRes.data().isEmpty()) {
            logger.error("Product reservation failed with message: {}",
                    reservationRes != null ? reservationRes.message() : "No response received");
            throw new ApiException("Failed to reserve products.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return reservationRes.data().stream()
                .collect(Collectors.toMap(ReserveStockItemResponse::productId, Function.identity()));
    }

    private void confirmReservation(UUID orderId) {
        ApiResponse<?> reservationRes = inventoryClient.confirmReservation(orderId);
        if (reservationRes == null || !reservationRes.success()) {
            logger.error("Failed to confirm order reservation for order: {} with reason: {}" , orderId,
                    reservationRes != null ? reservationRes.message() : "No response received");
            throw new ApiException("Failed to confirm order reservation.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void registerCreateOrderTransactionHooks(UUID orderId, UUID userId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.warn("Transaction synchronization is not active. Order creation hooks will not be registered.");
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    OrderCreationFailed event = new OrderCreationFailed(orderId, userId);
                    orderProducerService.sendOrderCreationFailedEvent(orderId, event);
                }
            }
        });
    }

    private void registerConfirmOrderTransactionHook(UUID orderId, UUID userId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.warn("Transaction synchronization is not active. Order confirmation hooks will not be registered.");
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                OrderConfirmed event = new OrderConfirmed(orderId, userId);
                orderProducerService.sendOrderConfirmedEvent(orderId, event);
            }

            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    OrderConfirmationFailed event = new OrderConfirmationFailed(orderId, userId);
                    orderProducerService.sendOrderConfirmationFailedEvent(orderId, event);
                }
            }
        });
    }

    private void registerCancelOrderTransactionHook(UUID orderId, UUID userId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.warn("Transaction synchronization is not active. Order cancellation hooks will not be registered.");
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                OrderCancelled event = new OrderCancelled(orderId, userId);
                orderProducerService.sendOrderCancelledEvent(orderId, event);
            }
        });
    }

    private void registerExpireOrdersTransactionHook(List<OrderExpiredEvent> expiredEvents) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.warn("Transaction synchronization is not active. Order expiration hooks will not be registered.");
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (OrderExpiredEvent event : expiredEvents) {
                    orderProducerService.sendOrderExpiredEvent(event.orderId(), event);
                }
            }
        });
    }

    private List<OrderItemCreateResponse> addItemsToOrderAndBuildResponse(List<CartItemResponse> cartItems,
                                                                          Map<UUID, BigDecimal> productToPrice,
                                                                          Map<UUID, ReserveStockItemResponse> productToReservation,
                                                                          Order order) {
        List<OrderItemCreateResponse> orderItemCreateResponses = new ArrayList<>();

        for (CartItemResponse cartItem : cartItems) {
            BigDecimal price = productToPrice.get(cartItem.productId());
            ReserveStockItemResponse reserveStockItemResponse = productToReservation.get(cartItem.productId());

            if (price != null && reserveStockItemResponse != null) {
                // Only add it to order if at least 1 item is reserved
                BigDecimal totalPrice = BigDecimal.ZERO;
                if (reserveStockItemResponse.reservedQuantity() > 0) {
                    OrderItem orderItem = OrderItem.builder()
                            .productId(reserveStockItemResponse.productId())
                            .price(price)
                            .quantity(reserveStockItemResponse.reservedQuantity())
                            .build();

                    orderItem.setTotalPrice(orderItem.calculateTotalPrice());
                    order.addItem(orderItem);

                    totalPrice = orderItem.getTotalPrice();
                }

                orderItemCreateResponses.add(OrderItemCreateResponse.from(reserveStockItemResponse, price, totalPrice));
            } else {
                orderItemCreateResponses.add(new OrderItemCreateResponse(
                        cartItem.productId(),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        cartItem.quantity(),
                        0,
                        OrderItemStatus.FAILED
                ));
                logger.warn("Price not found or reservation failed for product ID:{}", cartItem.productId());
            }
        }

        return orderItemCreateResponses;
    }
}
