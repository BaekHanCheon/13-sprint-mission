package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserType;

public record UserUpdateRequest(
        String newPassword,
        String newUsername,
        String newEmail,
        String phoneNumber,
        UserType userType
) {
}
