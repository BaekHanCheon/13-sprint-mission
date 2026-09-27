package com.sprint.mission.discodeit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.config.JwtProperties;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtAuthenticationFilterTest {

  private JwtTokenProvider tokenProvider;
  private UserDetailsService userDetailsService;
  private JwtAuthenticationFilter filter;
  private JwtRegistry jwtRegistry;
  private UserDetails userDetails;

  @BeforeEach
  void setUp() {
    JwtProperties properties = new JwtProperties();
    properties.setIssuer("discodeit");
    properties.setSecret("01234567890123456789012345678901");
    properties.setAccessTokenExpiration(Duration.ofMinutes(30));
    properties.setRefreshTokenExpiration(Duration.ofDays(7));
    tokenProvider = new JwtTokenProvider(properties);
    userDetailsService = mock(UserDetailsService.class);
    jwtRegistry = mock(JwtRegistry.class);
    filter = new JwtAuthenticationFilter(tokenProvider, userDetailsService, jwtRegistry);
    userDetails = User.withUsername("tester")
        .password("password")
        .roles("USER")
        .build();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void authenticateValidToken() throws Exception {
    String accessToken = tokenProvider.generateAccessToken(userDetails);
    when(userDetailsService.loadUserByUsername("tester")).thenReturn(userDetails);
    when(jwtRegistry.hasActiveJwtInformationByAccessToken(accessToken)).thenReturn(true);
    MockHttpServletRequest request = requestWithBearerToken(accessToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();

    filter.doFilter(request, response, filterChain);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
    assertThat(filterChain.getRequest()).isSameAs(request);
  }

  @Test
  void rejectInactiveToken() throws Exception {
    String accessToken = tokenProvider.generateAccessToken(userDetails);
    when(jwtRegistry.hasActiveJwtInformationByAccessToken(accessToken)).thenReturn(false);
    MockHttpServletRequest request = requestWithBearerToken(accessToken);

    filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(userDetailsService, never())
        .loadUserByUsername(org.mockito.ArgumentMatchers.anyString());
  }

  private static MockHttpServletRequest requestWithBearerToken(String token) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer " + token);
    return request;
  }
}
