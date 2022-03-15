package com.team701.buddymatcher.dtos.communication;

/**
 * A DTO with info about a message send from user to user
 */
public class MessageDTO {
    private String sender;
    private String receiver;
    private long timestamp;
    private String content;

    public MessageDTO(String sender, String receiver, long timestamp, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.content = content;
    }
}
