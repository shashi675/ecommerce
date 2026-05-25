package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ApiResponse;
import com.project.ecommerce.dto.CategoryRequest;
import com.project.ecommerce.dto.CategoryResponse;
import com.project.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestBody
            @Valid
            CategoryRequest request){

        log.info("Create Category Request: {}", request.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Category created",
                                service.createCategory(request)
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(){

        log.info("Get All Categories request");

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "All Categories found",
                        service.getAllCategories()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id){

        log.info("Get Category Request: {}", id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Category found",
                        service.getCategoryById(id)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> deleteCategory(@PathVariable Long id){

        log.info("Delete Category Request: {}", id);
        service.deleteCategory(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Category deleted",
                        null
                )
        );
    }
}