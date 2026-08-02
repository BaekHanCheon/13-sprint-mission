package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatusType;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
    @NotNull(message = "마지막 활동 시각은 필수입니다.") Instant newLastActiveAt,
    UserStatusType userStatus // 따로 로직 없어 일단 notnull 제외
) {

}
