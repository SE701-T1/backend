package com.team701.buddymatcher.dtos.users;

import java.io.Serializable;
import java.util.Objects;

/**
 * Data transfer object for the BlockedBuddies entity
 */
public class BlockedBuddiesDTO implements Serializable {
    private final Long id;
    private final UserDTO userBlocker;
    private final UserDTO userBlocked;

    public BlockedBuddiesDTO(Long id, UserDTO userBlocker, UserDTO userBlocked) {
        this.id = id;
        this.userBlocker = userBlocker;
        this.userBlocked = userBlocked;
    }

    public Long getId() {
        return id;
    }

    public UserDTO getUserBlocker() {
        return userBlocker;
    }

    public UserDTO getUserBlocked() {
        return userBlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockedBuddiesDTO entity = (BlockedBuddiesDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.userBlocker, entity.userBlocker) &&
                Objects.equals(this.userBlocked, entity.userBlocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userBlocker, userBlocked);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "userBlocker = " + userBlocker + ", " +
                "userBlocked = " + userBlocked + ")";
    }
}
