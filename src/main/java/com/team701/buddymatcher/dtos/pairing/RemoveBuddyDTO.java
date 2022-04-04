package com.team701.buddymatcher.dtos.pairing;

/**
 * Data Transfer Object representing a request to remove a buddy from a user
 */
public class RemoveBuddyDTO {

    private Long userId;
    private Long buddyId;

    public Long getUserId() {
        return this.userId;
    }

    public Long getBuddyId() {
        return this.buddyId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBuddyId(Long buddyId) {
        this.buddyId = buddyId;
    }

}