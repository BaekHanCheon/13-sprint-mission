package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {

    void createChannel(Channel channel);
    Channel readChannel(UUID id);
    void readAllChannel();
    void modifyChannel(Channel channel);
    void deleteChannel(UUID id);
}
