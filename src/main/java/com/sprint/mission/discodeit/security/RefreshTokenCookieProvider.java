package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.config.JwtProperties;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieProvider {

  public static final String COOKIE_NAME = "REFRESH_TOKEN";

  private final JwtProperties jwtProperties;

  public Cookie create(String refreshToken, boolean secure) {
    Cookie cookie = new Cookie(COOKIE_NAME, refreshToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(secure);
    cookie.setPath("/");
    cookie.setMaxAge(toCookieMaxAge(jwtProperties.getRefreshTokenExpiration()));
    return cookie;
  }

  public Cookie delete(boolean secure) {
    Cookie cookie = new Cookie(COOKIE_NAME, "");
    cookie.setHttpOnly(true);
    cookie.setSecure(secure);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    return cookie;
  }

  private static int toCookieMaxAge(Duration duration) {
    long seconds = duration.getSeconds();
    return Math.toIntExact(Math.min(seconds, Integer.MAX_VALUE));
  }
}
