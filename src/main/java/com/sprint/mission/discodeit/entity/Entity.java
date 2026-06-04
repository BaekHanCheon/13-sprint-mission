package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public abstract class Entity implements Serializable {
    private final UUID id;
    private long createdAt;
    private long updatedAt;

    public Entity(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return " " +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +"\n";
    }

}
