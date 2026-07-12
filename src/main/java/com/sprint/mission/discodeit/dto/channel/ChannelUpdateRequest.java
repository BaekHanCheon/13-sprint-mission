package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.lang.reflect.Type;
import java.util.UUID;

public record ChannelUpdateRequest(
        String newName,
        String newDescription
) { }
