package com.team701.buddymatcher.domain.users;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "USER_NAME", nullable = false)
    private String name;

    @Column(name = "USER_EMAIL", nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BUDDIES_ID")
    private Buddies buddies;

    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Buddies getBuddies() {
        return buddies;
    }

    public User setBuddies(Buddies buddies) {
        this.buddies = buddies;
        return this;
    }
}
