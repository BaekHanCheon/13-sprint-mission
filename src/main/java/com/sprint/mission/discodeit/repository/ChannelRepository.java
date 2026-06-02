package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {

    void createChannel(Channel channel);
    Channel findChannelById(UUID id);
    List<Channel> findAllChannel();
    void updateChannel(Channel channel);
    void deleteChannel(UUID id);
}
