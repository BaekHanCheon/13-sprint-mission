package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserType;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String userName,
        String email,
        String phoneNumber,
        UserType userType,
        Instant createdAt,
        Instant updatedAt,
        boolean isOnline
) {
    public static UserResponse from(User user){
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUserType(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isOnline()
        );
    }
}
