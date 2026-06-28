package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponse createMessage(MessageCreateRequest message, List<MultipartFile> attachments);
    MessageResponse findMessageById(UUID id);
    List<MessageResponse> findAllMessageByChannelId(UUID channelId);
    MessageResponse updateMessage(UUID messageId, MessageUpdateRequest request);
    void deleteMessage(UUID id);
}
