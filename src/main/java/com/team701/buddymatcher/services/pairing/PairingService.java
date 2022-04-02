package com.team701.buddymatcher.services.pairing;

import com.team701.buddymatcher.domain.users.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 *  Handles the incoming requests related to pairing of buddies endpoints
 */
public interface PairingService {
    void addBuddy(Long userId, Long buddyId);
    
    void removeBuddy(Long userId, Long buddyId);

    List<User> matchBuddy(Long userId, List<Long> courseIds);

    List<Long> getBuddyIds(Long userId);

    /**
     * Block a user. Remove existing match, and add the blocking user and blocked user paired to BLOCKED_BUDDIES tables.
     * @param userBlockerId the user ID of the user blocking the buddy user
     * @param userBlockedId the user ID of the buddy user being blocked
     * @throws NoSuchElementException when there is no User or Buddy
     */
    void blockBuddy(Long userBlockerId, Long userBlockedId) throws NoSuchElementException;
}
