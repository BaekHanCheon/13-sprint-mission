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
        String title,
        String description,
        ArrayList<UUID> allowedUserList,
        ChannelType type,
        Instant createdAt,
        Instant updatedAt,
        Instant lastMessageAt
) {
    public static ChannelResponse from(Channel channel, Instant lastMessageAt){
        List<UUID> allowedUserList = channel.getType() == ChannelType.PRIVATE
                ? channel.getAllowedUserList()   // PRIVATE이면 유저 목록 포함
                : Collections.emptyList();

        return new ChannelResponse(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getAllowedUserList(),
                channel.getType(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                lastMessageAt
        );
    }
}
