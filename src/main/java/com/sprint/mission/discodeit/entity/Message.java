package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "message")
public class Message extends BaseUpdatableEntity {

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private User author;

  @BatchSize(size = 50)
  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BinaryContent> attachment = new ArrayList<>();


  protected Message() {
  }

  public void updateAttachment(List<BinaryContent> attachment) {
    this.attachment = attachment;
  }

  public void updateContent(String content) {
    this.content = content;
  }

  public void updateAuthor(User author) {
    this.author = author;
  }

}
