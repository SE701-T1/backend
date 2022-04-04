package com.team701.buddymatcher.domain.users;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

/**
 * Entity model class for BLOCKED_BUDDIES database table
 * The order of users entered into the table is: (USER_BLOCKER_ID, USER_BLOCKED_ID)
 */
@Entity
@Table(name = "BLOCKED_BUDDIES", uniqueConstraints={
        @UniqueConstraint(columnNames = {"USER_BLOCKER_ID", "USER_BLOCKED_ID"})
})
public class BlockedBuddies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_BLOCKER_ID")
    private User userBlocker;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_BLOCKED_ID")
    private User userBlocked;

    /**
     * Getter method for class instance ID, or ID for the row in the BLOCKED_BUDDIES database table
     * @return the class instance ID, or ID for the row in the BLOCKED_BUDDIES database table
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter method for setting the class instance ID, or ID for the row in the BLOCKED_BUDDIES database table
     * @param id the class instance ID, or ID for the row in the BLOCKED_BUDDIES database table
     * @return the BlockedBuddies class instance
     */
    public BlockedBuddies setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter method for the blocking User class instance
     * @return the blocking User class instance
     */
    public User getUserBlocker() {
        return this.userBlocker;
    }

    /**
     * Setter method for setting the blocking User class instance
     * @param userBlocker blocking User class instance
     * @return the BlockedBuddies class instance
     */
    public BlockedBuddies setUserBlocker(User userBlocker) {
        this.userBlocker = userBlocker;
        return this;
    }

    /**
     * Getter method for the blocked User class instance
     * @return the blocked User class instance
     */
    public User getUserBlocked() {
        return this.userBlocked;
    }

    /**
     * Setter method for setting the blocked User class instance
     * @param userBlocked blocked User class instance
     * @return the BlockedBuddies class instance
     */
    public BlockedBuddies setUserBlocked(User userBlocked) {
        this.userBlocked = userBlocked;
        return this;
    }
}
