package com.myproject.myproject.service;

import com.myproject.myproject.model.ProductType;
import com.myproject.myproject.repository.ProductTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductTypeService.class);

    @Autowired
    private ProductTypeRepository productTypeRepository;

    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }

    public Optional<ProductType> findById(int id) {
        return productTypeRepository.findById(id);
    }

    public ProductType addProductType(ProductType ProductType) {
        return productTypeRepository.save(ProductType);
    }

    public ProductType updateProductType(ProductType ProductType) {
        return productTypeRepository.save(ProductType);
    }

    public void deleteProductType(int id) {
        findById(id)
                .ifPresent(ProductType -> productTypeRepository.delete(ProductType));
    }

    @Async
    public CompletableFuture<List<ProductType>> getAllProductTypeAsync() {
        LOGGER.info("Request to get a list of ProductType");
        final List<ProductType> ProductType = productTypeRepository.findAll();
        return CompletableFuture.completedFuture(ProductType);
    }
}
