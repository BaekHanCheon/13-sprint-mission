package com.sprint.mission.discodeit.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("discodeit.security.jwt")
public class JwtProperties {

  private String issuer = "discodeit";
  private String secret;
  private Duration accessTokenExpiration = Duration.ofMinutes(30);
  private Duration refreshTokenExpiration = Duration.ofDays(7);
  private int maxActiveJwtCount = 1;
}
