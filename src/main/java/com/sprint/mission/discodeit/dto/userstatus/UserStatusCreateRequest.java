package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId

) {
    public UserStatus toEntity(User user) {
        return new UserStatus(user);
    }
}
