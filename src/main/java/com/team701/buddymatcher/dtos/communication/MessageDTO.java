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
        return this.id;
    }

    public MessageDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public MessageDTO setSenderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public Long getReceiverId() {
        return this.receiverId;
    }

    public MessageDTO setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public MessageDTO setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public MessageDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getRead() {
        return this.isRead;
    }

    public MessageDTO setRead(Boolean read) {
        this.isRead = read;
        return this;
    }
}
