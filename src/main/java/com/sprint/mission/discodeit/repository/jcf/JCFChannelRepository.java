package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelData = new HashMap();

    @Override
    public void createChannel(Channel channel) {
        channelData.put(channel.getId(),channel);
    }

    @Override
    public Channel readChannel(UUID id) {
        channelData.get(id).toString();
        return channelData.get(id);
    }

    @Override
    public void readAllChannel() {

    }

    @Override
    public void modifyChannel(Channel channel) {
        channelData.put(channel.getId(), channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        channelData.remove(id);
    }
}
