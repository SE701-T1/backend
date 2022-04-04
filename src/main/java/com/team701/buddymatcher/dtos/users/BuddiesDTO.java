package com.team701.buddymatcher.dtos.users;

import java.util.List;

public class BuddiesDTO {

    private String id;
    private List<UserDTO> users;

    public String getId() {
        return this.id;
    }

    public BuddiesDTO setId(String id) {
        this.id = id;
        return this;
    }

    public List<UserDTO> getUsers() {
        return this.users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
