package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
        UUID id,
        Instant lastOnline,
        UserStatusType userStatus
) {
}
