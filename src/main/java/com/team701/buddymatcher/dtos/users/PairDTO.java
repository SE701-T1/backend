package com.team701.buddymatcher.dtos.users;

import java.util.List;

public class PairDTO {
    private String id;
    private List<UserDTO> users;

    public String getId() {
        return id;
    }

    public PairDTO setId(String id) {
        this.id = id;
        return this;
    }
}
