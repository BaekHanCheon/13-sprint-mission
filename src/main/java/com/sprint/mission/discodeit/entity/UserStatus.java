package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "user_status")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;

  @Enumerated(EnumType.STRING)
  private UserStatusType userStatus;

  @Builder
  public UserStatus(User user) {
    super();
    this.user = user;
    this.lastActiveAt = Instant.now();
    this.userStatus = UserStatusType.ONLINE;
  }

  protected UserStatus() {
  }

  public boolean isOnline() {
    return Instant.now().minusSeconds(300).isBefore(lastActiveAt);
  }

  public void updateLastOnline(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }

  public void updateUserStatus(UserStatusType userStatus) {
    this.userStatus = userStatus;
  }

}
