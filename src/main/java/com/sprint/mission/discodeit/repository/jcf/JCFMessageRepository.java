package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageData = new HashMap<>();

    @Override
    public void createMessage(Message message, User user, Channel channel) {
        messageData.put(message.getId(), message);
    }

    @Override
    public Message findMessageById(UUID id) {
        Message message = messageData.get(id);
        if (message == null) {
            throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        }
        return message;
    }

    @Override
    public List<Message> findAllMessage() {
        return messageData.values().stream().sorted(Comparator.comparing(Message::getCreatedAt)).toList();
    }

    @Override
    public void updateMessage(Message message) {
        messageData.put(message.getId(), message);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageData.remove(id);
    }
}