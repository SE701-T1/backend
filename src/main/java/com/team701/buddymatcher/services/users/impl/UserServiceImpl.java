package com.team701.buddymatcher.services.users.impl;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.users.UserRepository;
import com.team701.buddymatcher.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User retrieve(String id) {
        UUID uuid = UUID.fromString(id);
        return userRepository.findById(uuid).orElseThrow();
    }
}
