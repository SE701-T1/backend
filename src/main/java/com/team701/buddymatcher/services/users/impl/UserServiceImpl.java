package com.team701.buddymatcher.services.users.impl;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.users.UserRepository;
import com.team701.buddymatcher.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User retrieveById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User updatePairingEnabled(Long id, Boolean pairingEnabled) {
        int rowsUpdated = userRepository.updatePairingEnabled(id, pairingEnabled);
        if(rowsUpdated == 0) {
            throw new NoSuchElementException();
        }
        return retrieveById(id);
    }

    @Override
    public User retrieveByEmail(String email) {return userRepository.findUserByEmail(email);    }

    @Override
    public User addUser(String name, String email) {
        return userRepository.createUser(name, email);
    }
}
