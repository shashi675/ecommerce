package com.project.ecommerce.controller;

import com.project.ecommerce.dto.AddToCartRequest;
import com.project.ecommerce.dto.ApiResponse;
import com.project.ecommerce.dto.CartResponse;
import com.project.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addToCart(
            @RequestBody AddToCartRequest request){

        log.info("Add ToCart Request for productId: {}", request.getProductId());

        cartService.addToCart(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Added to cart",
                                null
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(){

        log.info("Get Cart Request");

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<CartResponse>(
                        true,
                        "Cart fetched",
                        cartService.getCart()
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteProductFromCart(@RequestParam Long productId){

        log.info("Delete Product Request: {}", productId);

        cartService.deleteProductFromCart(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        new ApiResponse<>(
                                true,
                                "product removed from cart",
                                null
                        )
                );
    }

    @DeleteMapping("/empty-cart")
    public ResponseEntity<ApiResponse<String>> emptyCart(){
        log.info("Empty Cart Request");
        cartService.clearCart();
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        new ApiResponse<>(
                                true,
                                "cart is emptied",
                                null
                        )
                );
    }
}