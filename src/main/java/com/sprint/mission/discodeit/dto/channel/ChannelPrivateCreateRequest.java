package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ChannelPrivateCreateRequest(
        ArrayList<UUID> participantIds
) {
    public Channel toEntity() {
    return Channel.privateChannelBuilder()
            .allowedUserList(participantIds)
            .build();
    }
}
