package com.sprint.mission.discodeit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.dto.auth.JwtDto;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.InMemoryJwtRegistry;
import com.sprint.mission.discodeit.security.JwtInformation;
import com.sprint.mission.discodeit.security.JwtRegistry;
import com.sprint.mission.discodeit.security.JwtTokenProvider;
import com.sprint.mission.discodeit.security.RefreshTokenCookieProvider;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthControllerRefreshTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;
  private JwtTokenProvider tokenProvider;
  private UserDetails userDetails;
  private JwtRegistry jwtRegistry;
  private UserService userService;
  private UserResponse user;

  @BeforeEach
  void setUp() {
    JwtProperties properties = new JwtProperties();
    properties.setIssuer("discodeit");
    properties.setSecret("01234567890123456789012345678901");
    properties.setAccessTokenExpiration(Duration.ofMinutes(30));
    properties.setRefreshTokenExpiration(Duration.ofDays(7));
    tokenProvider = new JwtTokenProvider(properties);
    RefreshTokenCookieProvider cookieProvider = new RefreshTokenCookieProvider(properties);
    jwtRegistry = new InMemoryJwtRegistry(properties, tokenProvider);
    userService = mock(UserService.class);
    AuthController controller = new AuthController(
        userService, tokenProvider, cookieProvider, jwtRegistry);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
    user = new UserResponse(
        UUID.randomUUID(), "tester", "tester@example.com", null, true, Role.USER);
    userDetails = new DiscodeitUserDetails(user, "password");
    when(userService.findUserById(user.id())).thenReturn(user);
  }

  @Test
  void refreshTokens() throws Exception {
    String oldRefreshToken = tokenProvider.generateRefreshToken(userDetails);
    String oldAccessToken = tokenProvider.generateAccessToken(userDetails);
    jwtRegistry.registerJwtInformation(
        new JwtInformation(user, oldAccessToken, oldRefreshToken));

    MvcResult result = mockMvc.perform(post("/api/auth/refresh")
            .cookie(new Cookie(RefreshTokenCookieProvider.COOKIE_NAME, oldRefreshToken)))
        .andExpect(status().isOk())
        .andReturn();

    MockHttpServletResponse response = result.getResponse();
    JwtDto body = objectMapper.readValue(response.getContentAsByteArray(), JwtDto.class);
    assertThat(body.userDto()).isEqualTo(user);
    assertThat(tokenProvider.validateAccessToken(body.accessToken())).isTrue();

    Cookie rotatedCookie = response.getCookie(RefreshTokenCookieProvider.COOKIE_NAME);
    assertThat(rotatedCookie.getValue()).isNotEqualTo(oldRefreshToken);
    assertThat(jwtRegistry.hasActiveJwtInformationByRefreshToken(oldRefreshToken)).isFalse();
    assertThat(jwtRegistry.hasActiveJwtInformationByRefreshToken(rotatedCookie.getValue())).isTrue();
  }

  @Test
  void rejectInvalidRefreshToken() throws Exception {
    mockMvc.perform(post("/api/auth/refresh")
            .cookie(new Cookie(RefreshTokenCookieProvider.COOKIE_NAME, "invalid-token")))
        .andExpect(status().isUnauthorized());
  }
}
