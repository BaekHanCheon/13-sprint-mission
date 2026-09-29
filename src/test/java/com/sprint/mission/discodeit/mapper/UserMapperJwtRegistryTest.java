package com.sprint.mission.discodeit.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.JwtRegistry;
import org.junit.jupiter.api.Test;

class UserMapperJwtRegistryTest {

  private final JwtRegistry jwtRegistry = mock(JwtRegistry.class);
  private final UserMapper mapper = new UserMapper(
      mock(BinaryContentMapper.class), jwtRegistry);
  private final User user = User.builder().username("user").email("user@example.com")
      .password("password").role(Role.USER).build();

  @Test
  void mapActiveUserAsOnline() {
    when(jwtRegistry.hasActiveJwtInformationByUserId(user.getId())).thenReturn(true);

    assertThat(mapper.toDto(user).online()).isTrue();
  }
}
