package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ApiResponse;
import com.project.ecommerce.dto.ProductRequest;
import com.project.ecommerce.dto.ProductResponse;
import com.project.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(
            @RequestBody @Valid ProductRequest request){

        log.info("Add Product Request: {}", request.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Product added",
                                service.addProduct(request)
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(){

        log.info("Get All Products Request");

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Success",
                        service.getAllProducts()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id){

        log.info("Get Product Request: {}", id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Success",
                        service.getProductById(id)
                )
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductsByCategoryId(@PathVariable Long categoryId){

        log.info("Get All Products By CategoryId Request: {}", categoryId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Success",
                        service.getAllProductsByCategoryId(categoryId)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByName(@RequestParam String productName){

        log.info("Get All Products By Name Request: {}", productName);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Success",
                        service.getProductsByProductName(productName)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProductById(@PathVariable Long id){

        log.info("Delete Product Request: {}", id);
        service.deleteProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "product deleted",
                        null
                )
        );
    }
}