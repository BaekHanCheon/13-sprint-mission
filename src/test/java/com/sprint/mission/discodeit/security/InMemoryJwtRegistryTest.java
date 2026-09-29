package com.sprint.mission.discodeit.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Role;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryJwtRegistryTest {

  private static final String SECRET = "01234567890123456789012345678901";

  private JwtTokenProvider tokenProvider;
  private InMemoryJwtRegistry registry;
  private UserResponse user;
  private DiscodeitUserDetails userDetails;

  @BeforeEach
  void setUp() {
    JwtProperties properties = properties();
    tokenProvider = new JwtTokenProvider(properties);
    registry = new InMemoryJwtRegistry(1, tokenProvider);
    user = new UserResponse(
        UUID.randomUUID(), "tester", "tester@example.com", null, false, Role.USER);
    userDetails = new DiscodeitUserDetails(user, "password");
  }

  @Test
  void replacePreviousLogin() {
    JwtInformation first = issueInformation();
    JwtInformation second = issueInformation();

    registry.registerJwtInformation(first);
    registry.registerJwtInformation(second);

    assertThat(registry.hasActiveJwtInformationByAccessToken(first.getAccessToken())).isFalse();
    assertThat(registry.hasActiveJwtInformationByUserId(user.id())).isTrue();
    assertThat(registry.hasActiveJwtInformationByAccessToken(second.getAccessToken())).isTrue();
    assertThat(registry.hasActiveJwtInformationByRefreshToken(second.getRefreshToken())).isTrue();
  }

  @Test
  void rotateOnce() {
    JwtInformation oldInformation = issueInformation();
    JwtInformation newInformation = issueInformation();
    registry.registerJwtInformation(oldInformation);

    assertThat(registry.rotateJwtInformation(
        oldInformation.getRefreshToken(), newInformation)).isTrue();
    assertThat(registry.rotateJwtInformation(
        oldInformation.getRefreshToken(), issueInformation())).isFalse();
    assertThat(registry.hasActiveJwtInformationByRefreshToken(newInformation.getRefreshToken()))
        .isTrue();
  }

  @Test
  void invalidateByUserId() {
    registry.registerJwtInformation(issueInformation());

    registry.invalidateJwtInformationByUserId(user.id());

    assertThat(registry.hasActiveJwtInformationByUserId(user.id())).isFalse();
  }

  @Test
  void clearExpiredTokens() {
    Instant now = Instant.parse("2026-09-27T00:00:00Z");
    JwtProperties properties = properties();
    JwtTokenProvider oldTokenProvider = new JwtTokenProvider(
        properties, Clock.fixed(now.minus(Duration.ofDays(8)), ZoneOffset.UTC));
    JwtTokenProvider validatingProvider = new JwtTokenProvider(
        properties, Clock.fixed(now, ZoneOffset.UTC));
    InMemoryJwtRegistry expiringRegistry = new InMemoryJwtRegistry(1, validatingProvider);
    String accessToken = oldTokenProvider.generateAccessToken(userDetails);
    String refreshToken = oldTokenProvider.generateRefreshToken(userDetails);
    expiringRegistry.registerJwtInformation(
        new JwtInformation(user, accessToken, refreshToken));

    expiringRegistry.clearExpiredJwtInformation();

    assertThat(expiringRegistry.hasActiveJwtInformationByUserId(user.id())).isFalse();
  }

  private JwtInformation issueInformation() {
    return new JwtInformation(
        user,
        tokenProvider.generateAccessToken(userDetails),
        tokenProvider.generateRefreshToken(userDetails));
  }

  private static JwtProperties properties() {
    JwtProperties properties = new JwtProperties();
    properties.setIssuer("discodeit");
    properties.setSecret(SECRET);
    properties.setAccessTokenExpiration(Duration.ofMinutes(30));
    properties.setRefreshTokenExpiration(Duration.ofDays(7));
    properties.setMaxActiveJwtCount(1);
    return properties;
  }
}
