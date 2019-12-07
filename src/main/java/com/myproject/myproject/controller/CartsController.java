package com.myproject.myproject.controller;

import com.myproject.myproject.model.Carts;
import com.myproject.myproject.service.CartsService;
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
public class CartsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartsController.class);

    @Autowired
    private CartsService cartsService;

    @GetMapping("/carts/{id}")
    public ResponseEntity<Carts> showById(@PathVariable(value = "id") int id) {
        return cartsService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @GetMapping("/carts")
    public ResponseEntity<List<Carts>> showAll() {
        return new ResponseEntity<>(cartsService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/carts")
    public ResponseEntity<Carts> create(@RequestBody Carts carts) {
        return ResponseEntity.ok(cartsService.addCarts(carts));
    }

    @PutMapping("/carts")
    public ResponseEntity<Carts> update(@RequestBody Carts carts) {
        return ResponseEntity.ok(cartsService.updateCarts(carts));
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") int id) {
        cartsService.deleteCarts(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/carts/async")
    public @ResponseBody
    CompletableFuture<ResponseEntity> showAllAsync() {
        return cartsService.getAllCartsAsync()
                .<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCartsFailure);
    }

    private static Function<Throwable, ResponseEntity<? extends List<Carts>>> handleGetCartsFailure = throwable -> {
        LOGGER.error("Failed to read records: {0}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
