package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface MessageService {

    void createMessage(Message message, User user, Channel channel);
    void readMessage(UUID id);
    void readAllMessage();
    void modifyMessage(UUID id, String property, String value);
    void deleteMessage(UUID id);
}
