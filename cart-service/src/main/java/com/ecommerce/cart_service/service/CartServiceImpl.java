package com.ecommerce.cart_service.service;


import com.ecommerce.cart_service.dto.CartItemResponse;
import com.ecommerce.cart_service.dto.CreateCartItemRequest;
import com.ecommerce.cart_service.dto.UpdateCartItemRequest;
import com.ecommerce.cart_service.entity.Cart;
import com.ecommerce.cart_service.entity.CartItem;
import com.ecommerce.cart_service.exception.ApiException;
import com.ecommerce.cart_service.repository.CartRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private static final Logger logger = LogManager.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<CartItemResponse> getCart(UUID userId) {
        Cart cart = getUserCart(userId);

        return cart.getItems().stream().map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCart(UUID userId) {
        Cart cart = getUserCart(userId);
        cartRepository.delete(cart);
    }

    @Transactional(readOnly = true)
    @Override
    public CartItemResponse getCartItem(UUID cartItemId, UUID userId) {
        Cart cart = getUserCart(userId);
        CartItem cartItem = getCartItemFromCart(cart, cartItemId);

        return CartItemResponse.from(cartItem);
    }

    @Override
    public CartItemResponse createCartItem(CreateCartItemRequest request, UUID userId) {
        Cart cart = cartRepository.findById(userId)
                        .orElse(Cart.builder()
                                .userId(userId)
                                .build());

        boolean itemExists = cart.getItems().stream()
                .anyMatch(cartItem -> cartItem.getProductId().equals(request.productId()));
        if (itemExists)
            throw new ApiException("Cart Item already exists.", HttpStatus.BAD_REQUEST);

        CartItem cartItem = CartItem.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .build();

        cart.addItem(cartItem);
        cartRepository.save(cart);

        CartItem savedItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.productId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Failed to save cart item.", HttpStatus.INTERNAL_SERVER_ERROR));

        return CartItemResponse.from(savedItem);
    }

    @Override
    public CartItemResponse updateCartItem(UpdateCartItemRequest request, UUID cartItemId, UUID userId) {
        Cart cart = getUserCart(userId);
        CartItem cartItem = getCartItemFromCart(cart, cartItemId);

        cartItem.setQuantity(request.quantity());
        cartRepository.save(cart);

        return CartItemResponse.from(cartItem);
    }

    @Override
    public void deleteCartItem(UUID cartItemId, UUID userId) {
        Cart cart = getUserCart(userId);
        CartItem cartItem = getCartItemFromCart(cart, cartItemId);

        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    private Cart getUserCart(UUID userId) {
       return cartRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Cart does not exist.", HttpStatus.NOT_FOUND));
    }

    private CartItem getCartItemFromCart(Cart cart, UUID cartItemId) {
        return cart.getItems().stream().filter(item -> item.getId().equals(cartItemId))
                .findFirst().orElseThrow(() -> new ApiException("Cart item does not exist.", HttpStatus.NOT_FOUND));
    }
}
