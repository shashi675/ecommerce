package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryRequest;
import com.project.ecommerce.dto.CategoryResponse;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.exception.CategoryAlreadyExitsException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryResponse createCategory(CategoryRequest request){

        if(repository
                .findByName(request.getName())
                .isPresent()){
            throw new CategoryAlreadyExitsException("Category already exists");
        }

        Category category = new Category();

        category.setName(request.getName());

        Category saved = repository.save(category);

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
                        .orElseThrow(() -> new ResourceNotFoundException("Category does not exist"));

        return mapToResponse(category);
    }

    public void deleteCategory(Long id){
        repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category does not exist"));

        repository.deleteById(id);
    }

    private CategoryResponse mapToResponse(Category category){

        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}