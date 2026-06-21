package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @AllArgsConstructor @Builder
public class Message extends Entity implements Serializable {

    private String content;
    private final UUID channelId;
    private final UUID authorId;
    private List<UUID> attachmentIds;

    @Override
    public String toString() {
        return "메세지 : " +
                "content='" + content + '\'' +
                ", channelID=" + channelId +
                ", authorId=" + authorId +
                "} " + super.toString();
    }

    public void updateAttachmentIds(List<UUID> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }
    public void updateContent(String content) {
        this.content = content;
    }

}
