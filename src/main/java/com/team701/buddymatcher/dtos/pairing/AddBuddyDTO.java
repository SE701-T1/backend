package com.team701.buddymatcher.dtos.pairing;

/**
 * Data Transfer Object representing a request to add a buddy to a user
 */
public class AddBuddyDTO {
    private String userId;
    private String buddyId;

    public String getUserId() {return userId;}

    public String getBuddyId() {
        return buddyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBuddyId(String buddyId) {
        this.buddyId = buddyId;
    }

}
