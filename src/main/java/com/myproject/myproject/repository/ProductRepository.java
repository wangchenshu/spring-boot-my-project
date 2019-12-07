package com.myproject.myproject.repository;

import com.myproject.myproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByOrderByIdAsc();
    List<Product> findByProductTypeId(int productTypeId);
    Optional<Product> findByName(String name);
    List<Product> findByNameContains(String name);
}