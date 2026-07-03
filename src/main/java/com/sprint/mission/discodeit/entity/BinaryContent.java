package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class BinaryContent extends BaseEntity {

  private String fileName;
  private UUID userId;
  private UUID messageId;

}
