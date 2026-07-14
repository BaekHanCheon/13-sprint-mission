package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository repository;
  private final UserMapper userMapper;

  @Transactional
  public UserResponse login(AuthLoginRequest request) {
    User user = repository.findAll().stream()
        .filter(u -> u.getUsername().equals(request.username()))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 유저입니다. username: " + request.username()));

    if (!user.getPassword().equals(request.password())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    user.updateOnline(true);

    return userMapper.toDto(user);
  }
}
