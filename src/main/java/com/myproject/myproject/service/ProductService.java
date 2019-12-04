package com.myproject.myproject.service;

import com.myproject.myproject.model.Product;
import com.myproject.myproject.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        findById(id)
                .ifPresent(product -> productRepository.delete(product));
    }

    @Async
    public CompletableFuture<List<Product>> getAllProductAsync() {
        LOGGER.info("Request to get a list of product");
        final List<Product> product = productRepository.findAll();
        return CompletableFuture.completedFuture(product);
    }
}
