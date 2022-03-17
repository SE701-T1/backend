package com.team701.buddymatcher.domain.communication;

import com.team701.buddymatcher.domain.users.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "MESSAGES")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SENDER_ID")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "RECEIVER_ID")
    private User receiver;
    private Timestamp timestamp;
    private String content;

    private Boolean isRead;

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Message setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Boolean getRead() {
        return isRead;
    }

    public Message setRead(Boolean read) {
        isRead = read;
        return this;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public Message setSender(User sender) {
        this.sender = sender;
        return this;
    }

    public User getReceiver() {
        return receiver;
    }

    public Message setReceiver(User receiver) {
        this.receiver = receiver;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }
}
