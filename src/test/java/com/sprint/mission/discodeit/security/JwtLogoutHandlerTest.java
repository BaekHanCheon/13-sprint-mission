package com.sprint.mission.discodeit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.config.JwtProperties;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtLogoutHandlerTest {

  @Test
  void invalidateTokens() {
    JwtProperties properties = new JwtProperties();
    properties.setRefreshTokenExpiration(Duration.ofDays(7));
    RefreshTokenCookieProvider cookieProvider = new RefreshTokenCookieProvider(properties);
    JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
    JwtRegistry jwtRegistry = mock(JwtRegistry.class);
    JwtLogoutHandler logoutHandler = new JwtLogoutHandler(
        cookieProvider, tokenProvider, jwtRegistry);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setCookies(new Cookie(RefreshTokenCookieProvider.COOKIE_NAME, "refresh-token"));
    MockHttpServletResponse response = new MockHttpServletResponse();
    UUID userId = UUID.randomUUID();
    when(jwtRegistry.hasActiveJwtInformationByRefreshToken("refresh-token")).thenReturn(true);
    when(tokenProvider.getUserId("refresh-token")).thenReturn(userId);

    logoutHandler.logout(request, response, null);

    Cookie cookie = response.getCookie(RefreshTokenCookieProvider.COOKIE_NAME);
    assertThat(cookie.getValue()).isEmpty();
    assertThat(cookie.getMaxAge()).isZero();
    verify(jwtRegistry).invalidateJwtInformationByUserId(userId);
  }
}
