package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategoryId(Long categoryId);

    @Query("""
    SELECT p
    FROM Product p
    WHERE LOWER(p.name)
    LIKE LOWER(
        CONCAT('%', :name, '%')
    )
""")
    List<Product> findByName(@Param("name") String productName);
}