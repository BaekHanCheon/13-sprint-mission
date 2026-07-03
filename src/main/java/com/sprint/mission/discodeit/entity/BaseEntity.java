package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

@Getter
public abstract class BaseEntity {

  @Id
  private final UUID id;

  @CreatedDate
  @Column(updatable = false)
  private Instant createdAt;


  public BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }


}
