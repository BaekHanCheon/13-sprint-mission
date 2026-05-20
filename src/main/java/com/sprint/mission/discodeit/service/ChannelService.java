package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {

    void createChannel(Channel channel);
    void readChannel(UUID id);
    void readAllChannel();
    void modifyChannel(UUID id, String property, String value);
    void deleteChannel(UUID id);

}
