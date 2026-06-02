package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageData = new HashMap<>();

    @Override
    public void createMessage(Message message, User user, Channel channel) {
        messageData.put(message.getId(), message);
    }

    @Override
    public Message readMessage(UUID id) {
        return messageData.get(id);
    }

    @Override
    public void readAllMessage() {
        System.out.println(messageData.values().toString());
    }

    @Override
    public void modifyMessage(Message message) {
        messageData.put(message.getId(), message);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageData.remove(id);
    }
}