package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.AuthApi;
import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthApi {

  private final AuthService authService;

  @Override
  @PostMapping("/login")
  public ResponseEntity<UserResponse> login(@Valid @RequestBody AuthLoginRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
  }
}
