package com.team701.buddymatcher.services.pairing.impl;

import com.team701.buddymatcher.services.pairing.PairingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for the PairingService which is a service providing the implementations of the methods
 * for the pairing endpoints
 */
@Service
public class PairingServiceImpl implements PairingService {

    @Override
    public void addBuddy(Long userId, Long buddyId) {
        //Currently just a blank implementation for testing endpoint call
        System.out.println(String.format("Buddy add request: %s, %s",userId,buddyId));
    }
    
    @Override
    public void removeBuddy(Long userId, Long buddyId) {
        //Currently just a blank implementation for testing endpoint call
        System.out.println(String.format("Buddy remove request: %s, %s",userId,buddyId));
    }

    @Override
    public void matchBuddy(Long userId, List<Long> courseIds) {
        //Currently just a blank implementation for testing endpoint call
        System.out.println(String.format("Buddy match request: %s, %s",userId,courseIds));
    }
}
