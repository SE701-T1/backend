package com.team701.buddymatcher.dtos.users;

public class UserDTO {

    private String id;
    private String name;
    private String email;
    private BuddiesDTO buddies;

    public String getId() {
        return id;
    }

    public UserDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public BuddiesDTO getBuddies() {
        return buddies;
    }

    public UserDTO setBuddies(BuddiesDTO buddies) {
        this.buddies = buddies;
        return this;
    }
}
