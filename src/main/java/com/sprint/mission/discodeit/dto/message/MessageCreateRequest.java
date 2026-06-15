package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        String content,
        UUID authorId,
        UUID channelId,
        List<BinaryContentCreateRequest> attachments // 첨부파일 여러 개
) {
    public Message toEntity() {
        return Message.builder()
                .content(content)
                .authorId(authorId)
                .channelId(channelId)
                .build();
    }
}
