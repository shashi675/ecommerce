package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ApiResponse;
import com.project.ecommerce.dto.ProductRequest;
import com.project.ecommerce.dto.ProductResponse;
import com.project.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(
            @RequestBody @Valid ProductRequest request){

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