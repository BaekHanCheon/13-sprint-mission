package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void createMessage(Message message);
    Optional<Message> findMessageById(UUID id);
    List<Message> findAllMessageByChannelId(UUID channelId);
    void updateMessage(Message message);
    void deleteMessage(UUID id);
}
