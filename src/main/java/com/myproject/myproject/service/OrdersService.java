package com.myproject.myproject.service;

import com.myproject.myproject.model.Orders;
import com.myproject.myproject.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OrdersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    public List<Orders> findAll() {
        return ordersRepository.findAll();
    }

    public Optional<Orders> findById(int id) {
        return ordersRepository.findById(id);
    }

    public Orders addOrders(Orders orders) {
        return ordersRepository.save(orders);
    }

    public Orders updateOrders(Orders orders) {
        return ordersRepository.save(orders);
    }

    public void deleteOrders(int id) {
        findById(id)
                .ifPresent(orders -> ordersRepository.delete(orders));
    }

    @Async
    public CompletableFuture<List<Orders>> getAllOrdersAsync() {
        LOGGER.info("Request to get a list of orders");
        final List<Orders> orders = ordersRepository.findAll();
        return CompletableFuture.completedFuture(orders);
    }
}
