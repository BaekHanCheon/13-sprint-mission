package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotNull(message = "채널 ID는 필수입니다.") UUID channelId,
        @NotNull(message = "사용자 ID는 필수입니다.") UUID userId,
        @NotNull(message = "마지막으로 읽은 시각은 필수입니다.") Instant lastReadAt
) {
}
