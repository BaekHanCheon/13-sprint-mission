package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserType;

public record UserCreateRequest(
    String password,
    String username,
    String email,
    String phoneNumber,
    UserType userType
) {

  public User toEntity() {
    return User.builder()
        .password(password)
        .username(username)
        .email(email)
        .phoneNumber(phoneNumber)
        .userType(userType)
        .build();
  }
}
