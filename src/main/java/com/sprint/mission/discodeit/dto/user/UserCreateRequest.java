package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record UserCreateRequest(
        String password,
        String userName,
        String email,
        String phoneNumber,
        UserType userType,
        BinaryContentCreateRequest profileImageRequest
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
