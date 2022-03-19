package com.team701.buddymatcher.dtos.communication;

import java.sql.Timestamp;

/**
 * A DTO with info about a chatroom the user is in
 * Used for displaying a list of the user's buddies and their most recent message
 */
public class ChatDTO {
    // ID of the user
    private Long id;
    // Name of the buddy in the chatroom
    private String name;
    private String lastMessage;
    private Timestamp timestamp;

    public Long getId() {
        return id;
    }

    public ChatDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChatDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public ChatDTO setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public ChatDTO setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
