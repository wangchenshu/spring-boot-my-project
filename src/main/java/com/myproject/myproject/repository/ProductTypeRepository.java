package com.myproject.myproject.repository;

import com.myproject.myproject.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
    List<ProductType> findAllByOrderByIdAsc();
    Optional<ProductType> findByName(String name);
    List<ProductType> findByNameContains(String name);
}