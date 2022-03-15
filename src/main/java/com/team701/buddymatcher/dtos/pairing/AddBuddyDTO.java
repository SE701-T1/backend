package com.team701.buddymatcher.dtos.pairing;

/**
 * Data Transfer Object representing a request to add a buddy to a user
 */
public class AddBuddyDTO {
    private Long userId;
    private Long buddyId;

    public Long getUserId() {return userId;}

    public Long getBuddyId() {
        return buddyId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBuddyId(Long buddyId) {
        this.buddyId = buddyId;
    }

}
