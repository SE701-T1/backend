package com.team701.buddymatcher.services.pairing.impl;

import com.team701.buddymatcher.domain.users.Buddies;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.users.UserRepository;
import com.team701.buddymatcher.services.pairing.PairingService;
import com.team701.buddymatcher.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Implementation for the PairingService which is a service providing the implementations of the methods
 * for the pairing endpoints
 */
@Service
public class PairingServiceImpl implements PairingService {

    private final UserRepository userRepository;

    private final UserService userService;

    @Autowired
    public PairingServiceImpl(UserRepository userRepository,  UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
    @Override
    @Transactional
    public void addBuddy(Long userId, Long buddyId) {
        userService.addBuddy(userId, buddyId);
    }
    
    @Override
    public void removeBuddy(Long userId, Long buddyId) {
        userService.deleteBuddy(userId, buddyId);
    }

    @Override
    public void matchBuddy(Long userId, List<Long> courseIds) {
        //Currently just a blank implementation for testing endpoint call
        System.out.println(String.format("Buddy match request: %s, %s",userId,courseIds));
    }
}
