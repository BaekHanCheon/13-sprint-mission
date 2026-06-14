package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        Instant createdAt,
        String fileName,
        UUID userId,
        UUID messageId
) {
    public static BinaryContentResponse from(BinaryContent binaryContent) {
        return new BinaryContentResponse(
                binaryContent.getId(),
                binaryContent.getCreatedAt(),
                binaryContent.getFileName(),
                binaryContent.getUserId(),
                binaryContent.getMessageId()
        );
    }
}
