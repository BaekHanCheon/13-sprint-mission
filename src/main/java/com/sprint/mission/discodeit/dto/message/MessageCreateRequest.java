package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID authorId,
    UUID channelId
) {

  public Message toEntity(User author, Channel channel) {
    return Message.builder()
        .content(content)
        .author(author)
        .channel(channel)
        .build();
  }
}
