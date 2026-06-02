package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void createMessage(Message message, User user, Channel channel);
    Message findMessageById(UUID id);
    List<Message> findAllMessage();
    void updateMessage(Message message);
    void deleteMessage(UUID id);
}
