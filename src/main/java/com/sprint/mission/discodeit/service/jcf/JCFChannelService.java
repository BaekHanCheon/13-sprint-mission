package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> channelData = new HashMap<>();

    public void addAllowedUserList (UUID channelId, UUID userId){ //프라이빗 채널 유저 등록
        Channel channel = channelData.get(channelId);
        channel.addAllowedUserList(userId);
    }

    public void disallowChannelList (UUID channelId, UUID userId){ //프라이빗 채널 유저 삭제
        Channel channel = channelData.get(channelId);
        channel.disallowedList(userId);
    }

    @Override
    public void createChannel(Channel channel) {
        channelData.put(channel.getId(),channel);
        System.out.println("created channel");
    }

    @Override
    public void readChannel(UUID id) {
        System.out.println("read channel : ");
        System.out.println(channelData.get(id).toString());
    }

    @Override
    public void readAllChannel() {
        System.out.println("read AllChannels : ");
        System.out.println(channelData.values().toString());
    }

    @Override
    public void modifyChannel(UUID id, String property, String value) {
        Channel channel = channelData.get(id);

        switch (property){
            case "type" : channel.updateType(value); System.out.println("modified type");break;
            case "name" : channel.updateName(value); System.out.println("modified name");break;
            case "description" : channel.updateDescription(value); System.out.println("modified description");break;
        }
        channel.updateUpdatedAt();
    }

    @Override
    public void deleteChannel(UUID id) {
        channelData.remove(id);
        System.out.println("deleted Channel");
    }
}
