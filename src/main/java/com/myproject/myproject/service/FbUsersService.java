package com.myproject.myproject.service;

import com.myproject.myproject.model.FbUsers;
import com.myproject.myproject.repository.FbUsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FbUsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FbUsersService.class);

    @Autowired
    private FbUsersRepository fbUsersRepository;

    public Optional<FbUsers> findByMessengerUserId(String messengerUserId) {
        return fbUsersRepository.findByMessengerUserId(messengerUserId);
    }

    public Optional<FbUsers> findById(int id) {
        return fbUsersRepository.findById(id);
    }

    public FbUsers addFbUsers(FbUsers fbUsers) {
        LOGGER.info("Add new fb user: " + fbUsers);
        return fbUsersRepository.save(fbUsers);
    }
}
