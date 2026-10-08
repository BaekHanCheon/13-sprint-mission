package com.sprint.mission.discodeit.event.notification;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.event.message.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.user.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

  private final NotificationRepository notificationRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;

  // AFTER_COMMIT에서 실행하므로 알림 저장에는 별도의 트랜잭션이 필요하다.
  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void on(MessageCreatedEvent event) {
    var notifications = readStatusRepository
        .findAllByChannelIdAndNotificationEnabledTrue(event.channelId()).stream()
        .filter(status -> !status.getUser().getId().equals(event.authorId()))
        .map(status -> {
          String channelName = status.getChannel().getName();
          if (channelName == null) {
            channelName = "비공개 채널";
          }
          return new Notification(status.getUser(),
              event.authorName() + " (#" + channelName + ")",
              event.content() == null ? "첨부파일 메시지" : event.content());
        })
        .toList();
    notificationRepository.saveAll(notifications);
  }

  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void on(RoleUpdatedEvent event) {
    userRepository.findById(event.userId()).ifPresent(user -> notificationRepository.save(
        new Notification(user, "권한이 변경되었습니다.",
            event.previousRole() + " -> " + event.newRole())));
  }
}
