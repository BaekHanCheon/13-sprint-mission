package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

public record ChannelPublicCreateRequest(
        String name,
        String description
) {
    public Channel toEntity() {
        return Channel.publicChannelBuilder()
                .type(ChannelType.PUBLIC)
                .name(name)
                .description(description)
                .build();
    }
}