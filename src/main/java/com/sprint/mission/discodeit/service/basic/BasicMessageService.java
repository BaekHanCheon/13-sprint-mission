package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository repository;
    private final ChannelRepository channelRepository;


    // 생성자로 Repository를 받아와서 저장(의존성 주입)
    public BasicMessageService(MessageRepository repository, ChannelRepository channelRepository) {
        this.repository = repository;
        this.channelRepository = channelRepository;
    }

    public boolean isPrivateChannelContainsAuthor(Message message, Channel channel) {
        if (channel.getType() == ChannelType.PRIVATE) {

            return channelRepository.findChannelById(channel.getId()).getAllowedUserList().contains(message.getAuthorId());
        }
        return true;
    }

    private boolean isAccessable(Message message, User user, Channel channel) {
        if (channel.getType() == ChannelType.PRIVATE) {
            if (!isPrivateChannelContainsAuthor(message, channel)) {
                System.out.println("접근 제한 채널 - 생성할 수 없습니다.");
                return false;
            }
        } else if (channel.getType() == ChannelType.MANAGER) {
            if (user.getUserType() != UserType.MANAGER) {
                System.out.println("매니저 전용 채널 - 생성할 수 없습니다.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void createMessage(Message message, User user, Channel channel) {
        if (!isAccessable(message, user, channel)) {
            return;
        }
        repository.createMessage(message, user, channel);
        System.out.println("메시지 생성됨");
    }

    @Override
    public void findMessageById(UUID id) {
        Message message = repository.findMessageById(id);
        if (message != null) {
            System.out.println("메세지 조회 -");
            System.out.println(message);
        } else {
            System.out.println("해당 메시지가 없습니다.");
        }
    }

    @Override
    public void findAllMessage() {
        System.out.println("메세지 전체조회 -" + repository.findAllMessage());

    }

    @Override
    public void updateMessage(UUID id, String property, String value) {
        Message message = repository.findMessageById(id);
        if (message == null) {
            System.out.println("해당 메시지가 없습니다.");
            return;
        }
        switch (property) {
            case "content": message.updateContent(value); System.out.println("modified content"); break;
        }
        message.updateUpdatedAt();
        repository.updateMessage(message);
    }

    @Override
    public void deleteMessage(UUID id) {
        repository.deleteMessage(id);
        System.out.println("메세지 삭제됨");
    }
}