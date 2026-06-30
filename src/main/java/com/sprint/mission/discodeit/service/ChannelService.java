package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponse createPublicChannel(ChannelPublicCreateRequest channel);
    ChannelResponse createPrivateChannel(ChannelPrivateCreateRequest channel);
    ChannelResponse findChannelById(UUID id);
    List<ChannelResponse> findAllByUserId(UUID userId);
    ChannelResponse updateChannel(UUID channelId, ChannelUpdateRequest request);
    void deleteChannel(UUID id);

    void addAllowedUserList(UUID id, UUID id1);
}
