package com.myproject.myproject.repository;

import com.myproject.myproject.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findAllByOrderByIdAsc();
}
