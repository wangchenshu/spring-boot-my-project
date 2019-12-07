package com.myproject.myproject.controller;

import com.myproject.myproject.model.Orders;
import com.myproject.myproject.service.OrdersService;
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
public class ChatfuelOrdersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatfuelOrdersController.class);

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/chatfuel/orders/{id}")
    public ResponseEntity<Orders> showById(@PathVariable(value = "id") int id) {
        return ordersService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @GetMapping("/chatfuel/orders")
    public ResponseEntity<List<Orders>> showAll() {
        return new ResponseEntity<>(ordersService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/chatfuel/orders")
    public ResponseEntity<Orders> create(@RequestBody Orders orders) {
        return ResponseEntity.ok(ordersService.addOrders(orders));
    }

    @PutMapping("/chatfuel/orders")
    public ResponseEntity<Orders> update(@RequestBody Orders orders) {
        return ResponseEntity.ok(ordersService.updateOrders(orders));
    }

//    @DeleteMapping("/chatfuel/orders/{id}")
//    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") int id) {
//        ordersService.deleteOrders(id);
//        return ResponseEntity.ok(true);
//    }

    @GetMapping("/chatfuel/orders/async")
    public @ResponseBody
    CompletableFuture<ResponseEntity> showAllAsync() {
        return ordersService.getAllOrdersAsync()
                .<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetOrdersFailure);
    }

    private static Function<Throwable, ResponseEntity<? extends List<Orders>>> handleGetOrdersFailure = throwable -> {
        LOGGER.error("Failed to read records: {0}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
