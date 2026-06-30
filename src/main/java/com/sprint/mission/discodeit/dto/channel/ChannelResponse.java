package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String name,
        String description,
        List<UUID> participantIds,
        ChannelType type,
        Instant createdAt,
        Instant updatedAt,
        Instant lastMessageAt
) {
    public static ChannelResponse from(Channel channel, Instant lastMessageAt){
        List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
                ? channel.getAllowedUserList()   // PRIVATE이면 참여자 목록 포함
                : Collections.emptyList();        // PUBLIC은 전체 공개이므로 빈 목록

        return new ChannelResponse(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                channel.getType(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                lastMessageAt
        );
    }
}
