package com.team701.buddymatcher.dtos.users;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private Boolean pairingEnabled;
    private Long buddyCount;

    public Long getId() {
        return id;
    }

    public UserDTO setId(Long id) {
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

    public Boolean getPairingEnabled() {
        return pairingEnabled;
    }

    public UserDTO setPairingEnabled(Boolean pairingEnabled) {
        this.pairingEnabled = pairingEnabled;
        return this;
    }

    public Long getBuddyCount() {
        return buddyCount;
    }

    public UserDTO setBuddyCount(Long buddyCount) {
        this.buddyCount = buddyCount;
        return this;
    }
}
