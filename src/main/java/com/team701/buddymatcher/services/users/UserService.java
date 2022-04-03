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
     * Report a user. Add the reporting user and reported user paired to REPORTED_BUDDIES tables with report information
     * @param userReportingId the user ID of the user reporting the buddy user
     * @param userReportedId  the user ID of the buddy user being reported
     * @param reportInfo      the report information given by the reporting user
     * @throws NoSuchElementException when there is no User or Buddy
     */
    void reportBuddy(Long userReportingId, Long userReportedId, String reportInfo) throws NoSuchElementException;
}
