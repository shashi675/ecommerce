package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductRequest;
import com.project.ecommerce.dto.ProductResponse;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.repository.CategoryRepository;
import com.project.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;


    public ProductResponse addProduct(ProductRequest request) {

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category Not Found with id: {}", request.getCategoryId());
                    return new ResourceNotFoundException("Category of the product not found");
                });

        log.info("Creating new product with category: {}", category.getName());

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        log.info("Product saved with id: {}", savedProduct.getId());

        return mapToResponse(savedProduct);
    }


    public List<ProductResponse> getAllProducts() {

        return productRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public ProductResponse getProductById(Long id) {

        Product product = productRepository
                        .findById(id)
                        .orElseThrow(() -> {
                            log.error("Product Not Found with id: {}", id);
                            return new ResourceNotFoundException("Product not found");
                        });

        return mapToResponse(product);
    }


    public void deleteProduct(Long id) {

        Product product = productRepository
                        .findById(id)
                        .orElseThrow(() -> {
                            log.error("Product Not Found with id: {}", id);
                            return new ResourceNotFoundException("Product not found");
                        });

        productRepository.delete(product);
        log.info("Product deleted with id: {}", id);
    }


    private ProductResponse mapToResponse(Product product) {

        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory().getName())
                .build();
    }
    public List<ProductResponse> getAllProductsByCategoryId(Long categoryId) {
        if(! categoryRepository.findById(categoryId).isPresent()) {
            log.error("Category Not Found with id: {}", categoryId);
            throw new ResourceNotFoundException("Category not found");
        }

        log.info("Getting all products by categoryId: {}:", categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProductResponse> getProductsByProductName(String productName) {
        List<Product> products = productRepository.findByName(productName);
        log.info("{} products found with name {}", products.size(), productName);
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }
}