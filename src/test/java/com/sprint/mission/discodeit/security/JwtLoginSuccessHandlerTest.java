package com.sprint.mission.discodeit.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.dto.auth.JwtDto;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Role;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class JwtLoginSuccessHandlerTest {

  @Test
  void returnTokens() throws Exception {
    JwtProperties properties = new JwtProperties();
    properties.setIssuer("discodeit");
    properties.setSecret("01234567890123456789012345678901");
    properties.setAccessTokenExpiration(Duration.ofMinutes(30));
    properties.setRefreshTokenExpiration(Duration.ofDays(7));
    JwtTokenProvider tokenProvider = new JwtTokenProvider(properties);
    RefreshTokenCookieProvider cookieProvider = new RefreshTokenCookieProvider(properties);
    JwtRegistry jwtRegistry = new InMemoryJwtRegistry(properties, tokenProvider);
    ObjectMapper objectMapper = new ObjectMapper();
    JwtLoginSuccessHandler handler = new JwtLoginSuccessHandler(
        objectMapper, tokenProvider, cookieProvider, jwtRegistry);
    UserResponse user = new UserResponse(
        UUID.randomUUID(), "tester", "tester@example.com", null, true, Role.USER);
    DiscodeitUserDetails principal = new DiscodeitUserDetails(user, "password");
    UsernamePasswordAuthenticationToken authentication =
        UsernamePasswordAuthenticationToken.authenticated(
            principal, principal.getPassword(), principal.getAuthorities());
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    handler.onAuthenticationSuccess(request, response, authentication);

    assertThat(response.getStatus()).isEqualTo(200);
    JwtDto body = objectMapper.readValue(response.getContentAsByteArray(), JwtDto.class);
    assertThat(body.userDto()).isEqualTo(user);
    assertThat(tokenProvider.validateAccessToken(body.accessToken())).isTrue();

    Cookie refreshCookie = response.getCookie(RefreshTokenCookieProvider.COOKIE_NAME);
    assertThat(tokenProvider.validateRefreshToken(refreshCookie.getValue())).isTrue();
    assertThat(jwtRegistry.hasActiveJwtInformationByAccessToken(body.accessToken())).isTrue();
  }
}
