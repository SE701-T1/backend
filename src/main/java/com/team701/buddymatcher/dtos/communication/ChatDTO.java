package com.team701.buddymatcher.dtos.communication;

import java.sql.Timestamp;

public class ChatDTO {
    private Long id;
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
