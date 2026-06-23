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
        UUID messageId,
        String contentType,
        String bytes
) {
    public static BinaryContentResponse from(BinaryContent binaryContent) {
        return from(binaryContent, null, null);
    }

    public static BinaryContentResponse from(BinaryContent binaryContent, String contentType, String bytes) {
        return new BinaryContentResponse(
                binaryContent.getId(),
                binaryContent.getCreatedAt(),
                binaryContent.getFileName(),
                binaryContent.getUserId(),
                binaryContent.getMessageId(),
                contentType,
                bytes
        );
    }
}
