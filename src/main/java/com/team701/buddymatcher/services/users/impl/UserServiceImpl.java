package com.team701.buddymatcher.services.users.impl;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.users.ReportedBuddiesRepository;
import com.team701.buddymatcher.repositories.users.BlockedBuddiesRepository;
import com.team701.buddymatcher.repositories.users.BuddiesRepository;
import com.team701.buddymatcher.repositories.users.UserRepository;
import com.team701.buddymatcher.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BuddiesRepository buddiesRepository;
    private final ReportedBuddiesRepository reportedBuddiesRepository;
    private final BlockedBuddiesRepository blockedBuddiesRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BuddiesRepository buddiesRepository,
                           BlockedBuddiesRepository blockedBuddiesRepository,
                           ReportedBuddiesRepository reportedBuddiesRepository) {
        this.userRepository = userRepository;
        this.buddiesRepository = buddiesRepository;
        this.blockedBuddiesRepository = blockedBuddiesRepository;
        this.reportedBuddiesRepository = reportedBuddiesRepository;
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
    public User retrieveByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void addUser(String name, String email) {
        userRepository.createUser(name, email);
    }

    @Override
    public List<User> retrieveBuddiesByUserId(Long userId) {
        return userRepository.findBuddies(userId);
    }

    @Override
    public Long countBuddies(User user) {
        return buddiesRepository.countByUser0OrUser1(user, user);
    }

    @Override
    public void addBuddy(Long currentUserId, Long buddyUserId) throws NoSuchElementException  {
        Long[] buddies = this.orderBuddyId(currentUserId, buddyUserId);

        userRepository.createBuddy(buddies[0], buddies[1]);
    }

    @Override
    public void deleteBuddy(Long currentUserId, Long buddyUserId)  throws NoSuchElementException {
        Long[] buddies = this.orderBuddyId(currentUserId, buddyUserId);

        userRepository.deleteBuddy(buddies[0], buddies[1]);
    }

    /**
     * OrderBuddyId in terms of userId_i < userId_j based on database structure.
     * This is to ensure we only have one relation per buddy pair.
     * @param user0Id ...
     * @param user1Id ...
     * @return Long[ user0Id, user1Id ] in order
     * @throws NoSuchElementException when there is no User or Buddy
     */
    private Long[] orderBuddyId(Long user0Id, Long user1Id) throws NoSuchElementException {
        if (user0Id.equals(user1Id)) {
            throw new NoSuchElementException("Cannot be a buddy of yourself");
        }
        if (user0Id > user1Id) {
            return new Long[] {user1Id, user0Id};
        }
        return new Long[] {user0Id, user1Id};
    }

    /**
     * Block a user. Remove existing match, and add the blocking user and blocked user paired to BLOCKED_BUDDIES tables.
     * @param userBlockerId the user ID of the user blocking the buddy user
     * @param userBlockedId the user ID of the buddy user being blocked
     * @throws NoSuchElementException when there is no User or Buddy
     */
    @Override
    public void blockBuddy(Long userBlockerId, Long userBlockedId) throws NoSuchElementException {
        // Remove blocker and blocked user buddies match if any exists
        List<User> buddies = retrieveBuddiesByUserId(userBlockerId);
        for (User buddy : buddies) {
            if (Objects.equals(buddy.getId(), userBlockedId)) {
                deleteBuddy(userBlockerId, userBlockedId);
                break;
            }
        }
        // Add blocker and blocked user pair to BLOCKED_BUDDIES database table
        blockedBuddiesRepository.addBlockedBuddy(userBlockerId, userBlockedId);
    }

    /**
     * Report a user. Add the reporting user and reported user paired to REPORTED_BUDDIES tables with report information
     * @param userReportingId the user ID of the user reporting the buddy user
     * @param userReportedId the user ID of the buddy user being reported
     * @param reportInfo the report information given by the reporting user
     * @throws NoSuchElementException when there is no User or Buddy
     */
    @Override
    public void reportBuddy(Long userReportingId, Long userReportedId, String reportInfo) throws NoSuchElementException {
        reportedBuddiesRepository.addReportedBuddy(userReportingId, userReportedId, reportInfo);
    }
}
