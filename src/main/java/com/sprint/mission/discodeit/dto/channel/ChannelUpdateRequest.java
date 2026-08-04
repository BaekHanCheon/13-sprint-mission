package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.lang.reflect.Type;
import java.util.UUID;
import jakarta.validation.constraints.Size;

public record ChannelUpdateRequest(
    @Size(max = 20, message = "채널 이름은 20자 이하여야 합니다.")
    String newName,
    @Size(min = 1, max = 30, message = "채널 설명은 30자 이하여야 합니다.")
    String newDescription
) {

}
