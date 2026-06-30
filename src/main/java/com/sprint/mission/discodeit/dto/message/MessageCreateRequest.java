package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageCreateRequest(
        String content,
        UUID authorId,
        UUID channelId
) {
    public Message toEntity() {
        return Message.builder()
                .content(content)
                .authorId(authorId)
                .channelId(channelId)
                .build();
    }
}
