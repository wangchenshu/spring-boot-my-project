package com.myproject.myproject.repository;

import com.myproject.myproject.model.FbUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FbUsersRepository extends JpaRepository<FbUsers, Integer> {
}
