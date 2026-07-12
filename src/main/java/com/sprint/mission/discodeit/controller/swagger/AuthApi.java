package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

  @Operation(summary = "로그인", description = "사용자명과 비밀번호로 로그인합니다.")
  ResponseEntity<UserResponse> login(AuthLoginRequest request);
}
