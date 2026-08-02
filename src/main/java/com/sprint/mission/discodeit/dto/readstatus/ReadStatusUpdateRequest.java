package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(
        @NotNull(message = "마지막으로 읽은 시각은 필수입니다.") Instant newLastReadAt
) { }
