package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserType;

import java.util.UUID;

public record UserUpdateRequest(
        String password,
        String userName,
        String email,
        String phoneNumber,
        UserType userType
) {
    public User toEntity() {
        return User.builder()
                .password(password)
                .userName(userName)
                .email(email)
                .phoneNumber(phoneNumber)
                .userType(userType)
                .build();
    }
}
