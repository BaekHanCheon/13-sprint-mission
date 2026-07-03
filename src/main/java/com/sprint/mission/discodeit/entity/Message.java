package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class Message extends BaseUpdatableEntity {

  private String content;
  private final UUID channelId;
  private final UUID authorId;
  private List<UUID> attachmentIds;

  public void updateAttachmentIds(List<UUID> attachmentIds) {
    this.attachmentIds = attachmentIds;
  }

  public void updateContent(String content) {
    this.content = content;
  }

}
