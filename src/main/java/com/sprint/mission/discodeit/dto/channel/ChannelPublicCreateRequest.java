package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

public record ChannelPublicCreateRequest(
        String title,
        String description
) {
    public Channel toEntity() {
        return Channel.publicChannelBuilder()
                .name(title)
                .description(description)
                .build();
    }
}