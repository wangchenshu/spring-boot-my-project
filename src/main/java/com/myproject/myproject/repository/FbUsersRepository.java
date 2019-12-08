package com.myproject.myproject.repository;

import com.myproject.myproject.model.FbUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FbUsersRepository extends JpaRepository<FbUsers, Integer> {
    Optional<FbUsers> findByMessengerUserId(String messengerUserId);
}
