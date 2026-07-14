package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.time.Instant;
import java.util.UUID;

public interface MessageService {

    MessageResponse createMessage(MessageCreateRequest message, List<MultipartFile> attachments);
    MessageResponse findMessageById(UUID id);
    PageResponse<MessageResponse> findAllMessageByChannelId(
        UUID channelId, Instant cursor, Pageable pageable);
    MessageResponse updateMessage(UUID messageId, MessageUpdateRequest request);
    void deleteMessage(UUID id);
}
