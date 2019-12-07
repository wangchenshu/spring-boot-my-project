package com.myproject.myproject.repository;

import com.myproject.myproject.model.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CartsRepository extends JpaRepository<Carts, Integer> {
    List<Carts> findAllByOrderByIdAsc();
    List<Carts> findByMessengerUserId(String messengerUserId);
    @Transactional
    void deleteByMessengerUserId(String messengerUserId);
}
