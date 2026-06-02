package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;


public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelData = new HashMap();

    @Override
    public void createChannel (Channel channel) throws RuntimeException{
        channelData.put(channel.getId(),channel);
    }

    @Override
    public Channel findChannelById(UUID id) {
        Channel channel = channelData.get(id);
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
        }
        return channel;
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
    public void deleteChannel(UUID id) {
        channelData.remove(id);
    }
}
