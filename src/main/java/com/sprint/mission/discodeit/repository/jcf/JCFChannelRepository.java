package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@Slf4j
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelData = new HashMap();

    @Override
    public void createChannel(Channel channel) {

        channelData.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findChannelById(UUID channelId) {
        Channel channel = channelData.get(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
        }
        return Optional.ofNullable(channel);

    }

    @Override
    public List<Channel> findAllChannel() {
        return channelData.values().stream().sorted(Comparator.comparing(Channel::getCreatedAt)).toList();
    }

    @Override
    public void updateChannel(Channel channel) {

        channelData.put(channel.getId(), channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {

        channelData.remove(channelId);
    }
}
