package com.team701.buddymatcher.services.pairing.impl;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.services.pairing.PairingService;
import com.team701.buddymatcher.services.timetable.TimetableService;
import com.team701.buddymatcher.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
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
        userService.addBuddy(userId, buddyId);
    }
    
    @Override
    public void removeBuddy(Long userId, Long buddyId) {
        userService.deleteBuddy(userId, buddyId);
    }

    @Override
    public List<User> matchBuddy(Long userId, List<Long> courseIds) {
        List<User> possibleBuddies = timetableService.getUsersFromCourseIds(courseIds);
        return possibleBuddies;
    }

    @Override
    public List<Long> getBuddyIds(Long userId) {
        List<User> currentBuddies = userService.retrieveBuddiesByUserId(userId);
        return currentBuddies.stream().map(user -> user.getId()).collect(Collectors.toList());
    }

    /**
     * Block a user. Remove existing match, and add the blocking user and blocked user paired to BLOCKED_BUDDIES tables.
     * @param userBlockerId the user ID of the user blocking the buddy user
     * @param userBlockedId the user ID of the buddy user being blocked
     * @throws NoSuchElementException when there is no User or Buddy
     */
    @Override
    public void blockBuddy(Long userBlockerId, Long userBlockedId) throws NoSuchElementException {
        userService.blockBuddy(userBlockerId, userBlockedId);
    }
}
