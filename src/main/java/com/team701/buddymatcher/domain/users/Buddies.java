package com.team701.buddymatcher.domain.users;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "BUDDIES")
public class Buddies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @OneToMany(mappedBy = "buddies", fetch = FetchType.LAZY)
    private List<User> users;

    public UUID getId() {
        return id;
    }

    public Buddies setId(UUID id) {
        this.id = id;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public Buddies setUsers(List<User> users) {
        this.users = users;
        return this;
    }
}