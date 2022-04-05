package com.team701.buddymatcher.services.pairing.impl;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.services.pairing.PairingService;
import com.team701.buddymatcher.services.timetable.TimetableService;
import com.team701.buddymatcher.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation for the PairingService which is a service providing the implementations of the methods
 * for the pairing endpoints
 */
@Service
public class PairingServiceImpl implements PairingService {

    private final TimetableService timetableService;
    private final UserService userService;

    @Autowired
    public PairingServiceImpl(TimetableService timetableService, UserService userService) {
        this.timetableService = timetableService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void addBuddy(Long userId, Long buddyId) {
        this.userService.addBuddy(userId, buddyId);
    }
    
    @Override
    public void removeBuddy(Long userId, Long buddyId) {
        this.userService.deleteBuddy(userId, buddyId);
    }

    public List<User> matchBuddy(Long userId, List<Long> courseIds) {
        return this.userService.getSortedPotentialBuddies(userId, courseIds);
    }

    @Override
    public List<Long> getBuddyIds(Long userId) {
        List<User> currentBuddies = this.userService.retrieveBuddiesByUserId(userId);
        return currentBuddies.stream().map(User::getId).collect(Collectors.toList());
    }
}
