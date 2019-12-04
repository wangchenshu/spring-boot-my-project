package com.myproject.myproject.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.myproject.myproject.model.Product;
import com.myproject.myproject.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://192.168.0.8")
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> showById(@PathVariable(value = "id") int id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> showAll() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PutMapping("")
    public ResponseEntity<Product> update(@RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/async")
    public @ResponseBody
    CompletableFuture<ResponseEntity> showAllAsync() {
        return productService.getAllProductAsync()
                .<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetProductFailure);
    }

    private static Function<Throwable, ResponseEntity<? extends List<Product>>> handleGetProductFailure = throwable -> {
        LOGGER.error("Failed to read records: {0}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
