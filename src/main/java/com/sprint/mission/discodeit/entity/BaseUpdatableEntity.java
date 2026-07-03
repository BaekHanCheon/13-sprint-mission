package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;

public class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  private Instant updatedAt;

}
