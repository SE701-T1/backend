package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.BlockedBuddies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Repository class for performing operations on the BlockedBuddies object
 */
public interface BlockedBuddiesRepository extends JpaRepository<BlockedBuddies, Long>,
        JpaSpecificationExecutor<BlockedBuddies> {

    /**
     * Add a new blocking user and blocked user pair to the BLOCKED_BUDDIES database table
     * The order of users entered into the table is: (USER_BLOCKER_ID, USER_BLOCKED_ID)
     * @param userBlockerId the user ID of the user blocking the buddy user
     * @param userBlockedId the user ID of the buddy user being blocked
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Blocked_Buddies (user_blocker_id, user_blocked_id) VALUES (:userBlockerId, :userBlockedId)",
            nativeQuery=true)
    void addBlockedBuddy(@Param("userBlockerId") Long userBlockerId, @Param("userBlockedId") Long userBlockedId);

    /**
     * Remove the blocking user and blocked user pair from the BLOCKED_BUDDIES database table so that the blocked user
     * is unblocked
     * The order of users entered into the table is: (USER_BLOCKER_ID, USER_BLOCKED_ID)
     * @param userBlockerId the user ID of the user blocking the buddy user
     * @param userBlockedId the user ID of the buddy user being blocked
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BlockedBuddies b WHERE b.userBlocker.id=:userBlockerId AND b.userBlocked.id=:userBlockedId")
    void removeBlockedBuddy(@Param("userBlockerId") Long userBlockerId, @Param("userBlockedId") Long userBlockedId);
}
