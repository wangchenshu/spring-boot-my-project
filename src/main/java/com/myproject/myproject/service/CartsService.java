package com.myproject.myproject.service;

import com.myproject.myproject.model.Carts;
import com.myproject.myproject.repository.CartsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CartsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartsService.class);

    @Autowired
    private CartsRepository cartsRepository;

    public List<Carts> findAll() {
        return cartsRepository.findAll();
    }

    public List<Carts> findByMessengerUserId(String messengerUserId) {
        return cartsRepository.findByMessengerUserId(messengerUserId);
    }

    public Optional<Carts> findById(int id) {
        return cartsRepository.findById(id);
    }

    public Carts addCarts(Carts carts) {
        return cartsRepository.save(carts);
    }

    public Carts updateCarts(Carts carts) {
        return cartsRepository.save(carts);
    }

    public void deleteCarts(int id) {
        findById(id)
                .ifPresent(carts -> cartsRepository.delete(carts));
    }

    public Long deleteByMessengerUserId(String messengerUserId) {
        return cartsRepository.deleteByMessengerUserId(messengerUserId);
    }

    @Async
    public CompletableFuture<List<Carts>> getAllCartsAsync() {
        LOGGER.info("Request to get a list of carts");
        final List<Carts> carts = cartsRepository.findAll();
        return CompletableFuture.completedFuture(carts);
    }
}
