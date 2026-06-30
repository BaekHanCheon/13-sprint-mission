package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class Entity implements Serializable {

  private final UUID id;
  private Instant createdAt;
  private Instant updatedAt;

  public Entity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
  }

  public void updateUpdatedAt() {
    this.updatedAt = Instant.now();
  }

}
