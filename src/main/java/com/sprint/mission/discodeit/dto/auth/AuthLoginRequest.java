package com.sprint.mission.discodeit.dto.auth;

public record AuthLoginRequest(
        String userName,
        String password
) {
}
