package com.team701.buddymatcher.dtos.users;

import java.util.List;

public class BuddiesDTO {
    private String id;
    private List<UserDTO> users;

    public String getId() {
        return id;
    }

    public BuddiesDTO setId(String id) {
        this.id = id;
        return this;
    }
}
