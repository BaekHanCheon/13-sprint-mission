package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class FileChannelService implements ChannelService {

    FileChannelRepository repository = new FileChannelRepository();

    public void addAllowedUserList(UUID channelId, UUID userId) {
        Channel channel = repository.readChannel(channelId);
        if (channel == null) {
            System.out.println("해당 채널이 없습니다.");
            return;
        }
        channel.addAllowedUserList(userId);
        repository.modifyChannel(channel);
    }

    public void disallowChannelList(UUID channelId, UUID userId) {
        Channel channel = repository.readChannel(channelId);
        if (channel == null) {
            System.out.println("해당 채널이 없습니다.");
            return;
        }
        channel.disallowedList(userId);
        repository.modifyChannel(channel);
    }

    @Override
    public void createChannel(Channel channel) {
        repository.createChannel(channel);
        System.out.println("채널 생성됨");
    }

    @Override
    public void readChannel(UUID id) {
        Channel channel = repository.readChannel(id);
        if (channel != null) {
            System.out.println("채널 조회 -");
            System.out.println(channel.toString());
        } else {
            System.out.println("해당 채널이 없습니다.");
        }
    }

    @Override
    public void readAllChannel() {
        System.out.println("전체 채널 조회");
        repository.readAllChannel();
    }

    @Override
    public void modifyChannel(UUID id, String property, String value) {
        Channel channel = repository.readChannel(id);
        if (channel == null) {
            System.out.println("해당 채널이 없습니다.");
            return;
        }
        switch (property) {
            case "type": channel.updateType(value); System.out.println("modified type"); break;
            case "name": channel.updateName(value); System.out.println("modified name"); break;
            case "description": channel.updateDescription(value); System.out.println("modified description"); break;
        }
        channel.updateUpdatedAt();
        repository.modifyChannel(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        repository.deleteChannel(id);
        System.out.println("채널 삭제됨");
    }
}