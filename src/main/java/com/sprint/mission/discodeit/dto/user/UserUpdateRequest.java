package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserType;

import java.util.UUID;

public record UserUpdateRequest(
        String newPassword,
        String newUsername,
        String newEmail,
        String phoneNumber,
        UserType userType
) {
    public User toEntity() {
        return User.builder()
                .password(newPassword)
                .userName(newUsername)
                .email(newEmail)
                .phoneNumber(phoneNumber)
                .userType(userType)
                .build();
    }
}
