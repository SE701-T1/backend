package com.team701.buddymatcher.services.users;

import com.team701.buddymatcher.domain.users.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {
    User retrieveById(Long id);
    User updatePairingEnabled(Long id, Boolean pairingEnabled);
    User retrieveByEmail(String email);
    void addUser(String name, String email);

    List<User> retrieveBuddiesByUserId(Long userId);
    Long countBuddies(User user);
    void addBuddy(Long currentUserId, Long buddyUserId) throws NoSuchElementException;
    void deleteBuddy(Long currentUserId, Long buddyUserId) throws NoSuchElementException;

    /**
     * Block a user. Remove existing match, and add the blocking user and blocked user paired to BLOCKED_BUDDIES tables.
     * @param userBlockerId the user ID of the user blocking the buddy user
     * @param userBlockedId the user ID of the buddy user being blocked
     * @throws NoSuchElementException when there is no User or Buddy
     */
    void blockBuddy(Long userBlockerId, Long userBlockedId) throws NoSuchElementException;

    /**
     * Get a list of users blocked by the user with ID userBlockingId
     * @param userBlockingId the ID for the blocking user
     * @return list of User being blocked by the user with ID userBlockingId
     * @throws NoSuchElementException when there is no User with ID matching userBlockingId
     */
    List<User> getBlockedBuddies(Long userBlockingId) throws NoSuchElementException;

    void unblockBuddy(Long userUnblockerId, Long userBlockedId) throws NoSuchElementException;
}
