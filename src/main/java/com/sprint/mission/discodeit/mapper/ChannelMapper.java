package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final UserMapper userMapper;

  public ChannelResponse toDto(Channel channel, List<User> participants, Instant lastMessageAt) {
    if (channel == null) {
      return null;
    }

    return new ChannelResponse(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participants.stream().map(userMapper::toDto).toList(),
        lastMessageAt
    );
  }
}
