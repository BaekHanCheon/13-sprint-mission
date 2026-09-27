package com.sprint.mission.discodeit.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final RefreshTokenCookieProvider refreshTokenCookieProvider;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry jwtRegistry;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    if (request.getCookies() != null) {
      Arrays.stream(request.getCookies())
          .filter(cookie -> cookie.getName().equals(RefreshTokenCookieProvider.COOKIE_NAME))
          .map(jakarta.servlet.http.Cookie::getValue)
          .filter(jwtRegistry::hasActiveJwtInformationByRefreshToken)
          .findFirst()
          .ifPresent(refreshToken -> {
            try {
              jwtRegistry.invalidateJwtInformationByUserId(
                  jwtTokenProvider.getUserId(refreshToken));
            } catch (IllegalArgumentException ignored) {
              // 유효하지 않은 쿠키라도 아래에서 항상 삭제한다.
            }
          });
    }
    response.addCookie(refreshTokenCookieProvider.delete(request.isSecure()));
  }
}
