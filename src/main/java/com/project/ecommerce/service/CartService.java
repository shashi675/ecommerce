package com.project.ecommerce.service;

import com.project.ecommerce.dto.AddToCartRequest;
import com.project.ecommerce.dto.CartItemResponse;
import com.project.ecommerce.dto.CartResponse;
import com.project.ecommerce.entity.Cart;
import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.repository.CartItemRepository;
import com.project.ecommerce.repository.CartRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final SecurityUtil securityUtil;


    public void addToCart(AddToCartRequest request) {

        User user = securityUtil.getCurrentUser();

        Cart cart = cartRepository
                .findByUser(user)
                .orElseGet(()->{
                    log.info("Create new cart for user: {}", user.getId());
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepository.save(c);
                });


        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> {
                    log.info("Product not found for productId: {}", request.getProductId());
                    return new ResourceNotFoundException("Product not found");
                });


        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);


        if(cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        else{
            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        }

        cartItemRepository.save(cartItem);
        log.info("Added ToCart for user: {}", user.getId());
    }

    public CartResponse getCart() {
        User user = securityUtil.getCurrentUser();

        Cart cart = cartRepository
                .findByUser(user)
                .orElse(null);

        if(cart == null) {
            log.error("Cart not found for user: {}", user.getId());
            throw new ResourceNotFoundException("Cart not found");
        }

        return mapToResponse(cart);
    }

    public void deleteProductFromCart(Long productId)  {
        User user = securityUtil.getCurrentUser();

        Cart cart = cartRepository
                .findByUser(user)
                .orElseThrow(() -> {
                    log.error("Cart not found for user: {}", user.getId());
                    return new ResourceNotFoundException("Cart not found");
                });

        CartItem cartItem = cartItemRepository
                .findByCartAndProductId(cart, productId)
                .orElseThrow(() -> {
                    log.error("CartItem not found for user: {}", user.getId());
                    return new ResourceNotFoundException("Item not found in cart");
                });

        cartItemRepository.delete(cartItem);
        log.info("Product {} has been deleted from cart for user {}", productId, user.getId());
    }

    public void clearCart() {
        User user = securityUtil.getCurrentUser();
        Cart cart = cartRepository
                .findByUser(user)
                .orElseThrow(() -> {
                    log.error("Cart not found for user: {}", user.getId());
                    return new ResourceNotFoundException("Cart not found");
                });

        cartRepository.delete(cart);
        log.info("Cart has been deleted for user: {}", user.getId());
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getCartItems()
                .stream()
                .map(this::mapToCartItemResponse).toList();

        BigDecimal totalPrice = items
                .stream()
                .reduce(BigDecimal.ZERO,
                        (a, b) ->
                        a.add(b.getSubtotal()), BigDecimal::add);

        return CartResponse
                .builder()
                .cartId(cart.getId())
                .totalAmount(totalPrice)
                .items(items)
                .build();
    }


    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        return CartItemResponse
                .builder()
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getProduct().getPrice())
                .subtotal(
                        cartItem.getProduct().getPrice()
                                .multiply(
                                        BigDecimal.valueOf(cartItem.getQuantity()
                                        ))
                )
                .build();
    }
}
