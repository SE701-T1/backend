package com.team701.buddymatcher.domain.users;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PAIR")
public class Pair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @OneToMany(mappedBy = "pair", fetch = FetchType.LAZY)
    private List<User> users;

    public UUID getId() {
        return id;
    }

    public Pair setId(UUID id) {
        this.id = id;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public Pair setUsers(List<User> users) {
        this.users = users;
        return this;
    }
}
