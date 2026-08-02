package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelPublicCreateRequest(
    @NotBlank(message = "채널 이름은 필수입니다.")
    @Size(max = 20, message = "채널 이름은 20자 이하여야 합니다.")
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
