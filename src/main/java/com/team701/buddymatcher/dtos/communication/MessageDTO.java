package com.team701.buddymatcher.dtos.communication;

import java.util.Date;

/**
 * A DTO with info about a message send from user to user
 */
public class MessageDTO {
    private String sender;
    private String receiver;
    private Date timestamp;
    private String content;

    public MessageDTO(String sender, String receiver, Date timestamp, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.content = content;
    }
}
