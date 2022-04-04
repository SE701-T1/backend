package com.team701.buddymatcher.socket;

public class MessageObject {

    private Long senderId;
    private Long receiverId;
    private String message;

    public Long getSenderId() {
        return this.senderId;
    }

    public MessageObject setSenderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public Long getReceiverId() {
        return this.receiverId;
    }

    public MessageObject setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public MessageObject setMessage(String message) {
        this.message = message;
        return this;
    }
}
