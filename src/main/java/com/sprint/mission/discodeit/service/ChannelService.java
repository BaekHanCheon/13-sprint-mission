package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {

    void createChannel(Channel channel);
    void findChannelById(UUID id);
    void findAllChannel();
    void updateChannel(UUID id, String property, String value);
    void deleteChannel(UUID id);

    void addAllowedUserList(UUID id, UUID id1);
}
