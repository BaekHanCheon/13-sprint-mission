package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthLoginRequest(
    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(max = 10, message = "사용자 이름은 10자 이하여야 합니다.")
    String username,
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
    String password
) {

}
