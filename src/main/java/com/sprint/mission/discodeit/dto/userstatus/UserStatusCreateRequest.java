package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId

) {
    public UserStatus toEntity() {

        return UserStatus.builder()
                .userId(userId)
                .build();
    }
}
