package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.user.UserResponse;
import java.util.Objects;
import lombok.Getter;

@Getter
public class JwtInformation {

  private final UserResponse userDto;
  private volatile String accessToken;
  private volatile String refreshToken;

  public JwtInformation(UserResponse userDto, String accessToken, String refreshToken) {
    this.userDto = Objects.requireNonNull(userDto, "userDto");
    this.accessToken = Objects.requireNonNull(accessToken, "accessToken");
    this.refreshToken = Objects.requireNonNull(refreshToken, "refreshToken");
  }

  public synchronized void rotate(String accessToken, String refreshToken) {
    this.accessToken = Objects.requireNonNull(accessToken, "accessToken");
    this.refreshToken = Objects.requireNonNull(refreshToken, "refreshToken");
  }
}
