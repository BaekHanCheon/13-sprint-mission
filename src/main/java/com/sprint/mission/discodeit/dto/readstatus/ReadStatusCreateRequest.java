package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID channelId,
        UUID userId,
        Instant lastReadAt
) {
}
