package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private final UUID id;
    private final Instant createdAt;
    private String fileName;
    private UUID userId;
    private UUID messageId;

    @Builder
    public BinaryContent(String fileName , UUID userId, UUID messageId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.userId = userId;
        this.messageId = messageId;
    }
}
