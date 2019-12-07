package com.myproject.myproject.controller;

import com.myproject.myproject.model.ProductType;
import com.myproject.myproject.service.ProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RestController
//@CrossOrigin(origins = "http://192.168.0.8")
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class ProductTypeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductTypeController.class);

    @Autowired
    private ProductTypeService productTypeService;

    @GetMapping("/product-type/{id}")
    public ResponseEntity<ProductType> showById(@PathVariable(value = "id") int id) {
        return productTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @GetMapping("/product-types")
    public ResponseEntity<List<ProductType>> showAll() {
        return new ResponseEntity<>(productTypeService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/product-type")
    public ResponseEntity<ProductType> create(@RequestBody ProductType productType) {
        return ResponseEntity.ok(productTypeService.addProductType(productType));
    }

    @PutMapping("/product-type")
    public ResponseEntity<ProductType> update(@RequestBody ProductType productType) {
        return ResponseEntity.ok(productTypeService.updateProductType(productType));
    }

    @DeleteMapping("/product-type/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") int id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/product-types/async")
    public @ResponseBody
    CompletableFuture<ResponseEntity> showAllAsync() {
        return productTypeService.getAllProductTypeAsync()
                .<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetProductTypeFailure);
    }

    private static Function<Throwable, ResponseEntity<? extends List<ProductType>>> handleGetProductTypeFailure = throwable -> {
        LOGGER.error("Failed to read records: {0}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
