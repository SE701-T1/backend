package com.team701.buddymatcher.dtos.communication;

import java.sql.Timestamp;

/**
 * A DTO with info about a message sent between users
 */
public class MessageDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Timestamp timestamp;
    private String content;
    private Boolean isRead;

    public Long getId() {
        return id;
    }

    public MessageDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSenderId() {
        return senderId;
    }

    public MessageDTO setSenderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public MessageDTO setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public MessageDTO setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MessageDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getRead() {
        return isRead;
    }

    public MessageDTO setRead(Boolean read) {
        isRead = read;
        return this;
    }
}
