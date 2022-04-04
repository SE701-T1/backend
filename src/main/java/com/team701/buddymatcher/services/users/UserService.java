package com.team701.buddymatcher.services.users;

import com.team701.buddymatcher.domain.users.User;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {

    User retrieveById(Long id) throws NoSuchElementException;

    User updatePairingEnabled(Long id, Boolean pairingEnabled);

    User retrieveByEmail(String email);

    void addUser(String name, String email) throws DataIntegrityViolationException;

    List<User> retrieveBuddiesByUserId(Long userId);

    Long countBuddies(User user);

    void addBuddy(Long currentUserId, Long buddyUserId);

    void deleteBuddy(Long currentUserId, Long buddyUserId);

    /**
     * Block a user. Remove existing match, and add the blocking user and blocked user paired to BLOCKED_BUDDIES tables.
     * @param userBlockerId the user ID of the user blocking the buddy user
     * @param userBlockedId the user ID of the buddy user being blocked
     */
    void blockBuddy(Long userBlockerId, Long userBlockedId);

    /**
     * Report a user. Add the reporting user and reported user paired to REPORTED_BUDDIES tables with report information
     * @param userReportingId the user ID of the user reporting the buddy user
     * @param userReportedId  the user ID of the buddy user being reported
     * @param reportInfo      the report information given by the reporting user
     */
    void reportBuddy(Long userReportingId, Long userReportedId, String reportInfo);

    /**
     * Get a list of users blocked by the user with ID userBlockingId
     * @param userBlockingId the ID for the blocking user
     * @return list of User being blocked by the user with ID userBlockingId
     */
    List<User> getBlockedBuddies(Long userBlockingId);

    void unblockBuddy(Long userBlockerId, Long userBlockedId);
}
