package com.team701.buddymatcher.domain.communication;

import com.team701.buddymatcher.domain.users.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
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

    public Message setId(Long id) {
        this.id = id;
        return this;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public Message setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Boolean getRead() {
        return this.isRead;
    }

    public Message setRead(Boolean read) {
        this.isRead = read;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public User getSender() {
        return this.sender;
    }

    public Message setSender(User sender) {
        this.sender = sender;
        return this;
    }

    public User getReceiver() {
        return this.receiver;
    }

    public Message setReceiver(User receiver) {
        this.receiver = receiver;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }
}
