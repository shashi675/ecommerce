package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryRequest;
import com.project.ecommerce.dto.CategoryResponse;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.exception.CategoryAlreadyExitsException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryResponse createCategory(CategoryRequest request){

        log.info("Creating new category: {}", request.getName());
        if(repository
                .findByName(request.getName())
                .isPresent()){

            log.error("Category already exists with name: {}", request.getName());
            throw new CategoryAlreadyExitsException("Category already exists");
        }

        Category category = new Category();

        category.setName(request.getName());

        Category saved = repository.save(category);

        log.info("Category created with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    public List<CategoryResponse> getAllCategories(){

        return repository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id){

        Category category = repository
                        .findById(id)
                        .orElseThrow(() -> {
                            log.error("Category not found with id: {}", id);

                            return new ResourceNotFoundException("Category does not exist");
                        });

        log.info("Category found with id: {}", id);
        return mapToResponse(category);
    }

    public void deleteCategory(Long id){
        repository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category does not exist");
                });

        repository.deleteById(id);
        log.info("Category deleted with id: {}", id);
    }

    private CategoryResponse mapToResponse(Category category){

        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}