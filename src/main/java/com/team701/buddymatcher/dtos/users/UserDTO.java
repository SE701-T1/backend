package com.team701.buddymatcher.dtos.users;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private Boolean pairingEnabled = false;
    private Long buddyCount;

    public Long getId() {
        return this.id;
    }

    public UserDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public UserDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public Boolean getPairingEnabled() {
        return this.pairingEnabled;
    }

    public UserDTO setPairingEnabled(Boolean pairingEnabled) {
        this.pairingEnabled = pairingEnabled;
        return this;
    }

    public Long getBuddyCount() {
        return this.buddyCount;
    }

    public UserDTO setBuddyCount(Long buddyCount) {
        this.buddyCount = buddyCount;
        return this;
    }
}
