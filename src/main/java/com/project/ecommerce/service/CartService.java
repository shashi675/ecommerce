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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepository.save(c);
                });


        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found")
                );


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
    }

    public CartResponse getCart() {
        Cart cart = cartRepository
                .findByUser(securityUtil.getCurrentUser())
                .orElse(null);

        if(cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        return mapToResponse(cart);
    }

    public void deleteProductFromCart(Long productId)  {
        User user = securityUtil.getCurrentUser();

        Cart cart = cartRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        ));

        CartItem cartItem = cartItemRepository
                .findByCartAndProductId(cart, productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item not found in cart"
                        ));

        cartItemRepository.delete(cartItem);
    }

    public void clearCart() {
        User user = securityUtil.getCurrentUser();
        Cart cart = cartRepository
                .findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartRepository.delete(cart);
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
