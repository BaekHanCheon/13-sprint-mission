package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageData = new HashMap<>();

    @Override
    public void createMessage(Message message) {
        messageData.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findMessageById(UUID id) {
        Message message = messageData.get(id);
        if (message == null) {
            throw new IllegalArgumentException("메세지를 찾을 수 없습니다.");
        }
        return Optional.ofNullable(message);
    }

    @Override
    public List<Message> findAllMessageByChannelId(UUID channelId) {
        //return load().values().stream().sorted(Comparator.comparing(Message::getCreatedAt)).toList();
        return messageData.values().stream().filter(message -> message.getChannelId().equals(channelId)).toList();
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