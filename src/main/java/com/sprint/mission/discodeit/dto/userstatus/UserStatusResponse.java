package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        UUID userId,
        UserStatusType userStatusType,
        Instant lastOnline,
        boolean online,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserStatusResponse from(UserStatus userStatus) {
        return new UserStatusResponse(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getUserStatus(),
                userStatus.getLastOnline(),
                userStatus.isOnline(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt()
        );
    }
}
