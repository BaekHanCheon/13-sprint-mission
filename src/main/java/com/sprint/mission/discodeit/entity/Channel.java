package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@Table(name = "channel")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  @Column(length = 20)
  private String name;

  @Column(length = 20)
  private String description;

  private ArrayList<UUID> allowedUserList = new ArrayList<>();

  @BatchSize(size = 50)
  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  protected Channel() {
  }

  @Builder(builderMethodName = "publicChannelBuilder")
  public Channel(ChannelType type, String name, String description) {
    super();
    this.type = type;
    this.name = name;
    this.description = description;
  }

  @Builder(builderMethodName = "privateChannelBuilder")
  public Channel(ArrayList<UUID> allowedUserList) {
    super();
    this.type = ChannelType.PRIVATE;
    this.allowedUserList = allowedUserList != null
        ? new ArrayList<>(allowedUserList) : new ArrayList<>();
  }

  public void addAllowedUserList(UUID uid) {
    this.allowedUserList.add(uid);
  }

  public void disallowedList(UUID uid) {
    this.allowedUserList.remove(uid);
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public void updateType(ChannelType type) {
    if (type.equals(ChannelType.PUBLIC)) {
      this.type = ChannelType.PUBLIC;
    } else if (type.equals(ChannelType.PRIVATE)) {
      this.type = ChannelType.PRIVATE;
    } else if (type.equals(ChannelType.MANAGER)) {
      this.type = ChannelType.MANAGER;
    } else {
      System.out.println("올바른 채널 타입이 아닙니다.");
    }
  }
}
