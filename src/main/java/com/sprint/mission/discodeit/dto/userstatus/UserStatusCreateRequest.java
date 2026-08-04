package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserStatusCreateRequest(
        @NotNull(message = "사용자 ID는 필수입니다.") UUID userId

) {
    public UserStatus toEntity(User user) {
        return new UserStatus(user);
    }
}
