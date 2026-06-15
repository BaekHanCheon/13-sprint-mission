package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageResponse(
        UUID id,
        String content,
        UUID channelId,
        UUID authorId
) {
    public static MessageResponse from(Message message){
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId()
        );
    }
}
