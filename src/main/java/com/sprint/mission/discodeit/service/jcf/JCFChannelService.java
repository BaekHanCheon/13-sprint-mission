package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    //private final Map<UUID, Channel> channelData = new HashMap<>();
    JCFChannelRepository repository = new JCFChannelRepository();

    public void addAllowedUserList (UUID channelId, UUID userId){ //프라이빗 채널 유저 등록
        Channel channel = repository.findChannelById(channelId);
        channel.addAllowedUserList(userId);
    }

    public void disallowChannelList (UUID channelId, UUID userId){ //프라이빗 채널 유저 삭제
        Channel channel = repository.findChannelById(channelId);
        channel.disallowedList(userId);
    }

    @Override
    public void createChannel(Channel channel) {
        repository.createChannel(channel);
        //channelData.put(channel.getId(),channel);
        System.out.println("created channel");
    }

    @Override
    public void findChannelById(UUID id) {
        System.out.println("read channel : ");
        repository.findChannelById(id);
        //System.out.println(channelData.get(id).toString());
    }

    @Override
    public void findAllChannel() {
        System.out.println("read AllChannels : "+repository.findAllChannel());

        //System.out.println(channelData.values().toString());
    }

    @Override
    public void updateChannel(UUID id, String property, String value) {
        Channel channel = repository.findChannelById(id);

        switch (property){
            case "type" : channel.updateType(value); System.out.println("modified type");break;
            case "name" : channel.updateName(value); System.out.println("modified name");break;
            case "description" : channel.updateDescription(value); System.out.println("modified description");break;
        }
        channel.updateUpdatedAt();
        repository.updateChannel(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        //channelData.remove(id);
        repository.deleteChannel(id);
        System.out.println("deleted Channel");
    }
}
