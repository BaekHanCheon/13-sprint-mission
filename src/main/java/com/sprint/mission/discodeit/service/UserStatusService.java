package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService {

  private final UserStatusRepository repository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  public UserStatusResponse createUserStatus(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("User not found: " + request.userId()));
    UserStatus userStatus = request.toEntity(user);
    /*
    userRepository.findUserById(userStatus.getUser().getId())
        .orElseThrow(() -> new NoSuchElementException("해당 User가 없습니다."));

    if (repository.findUserStatusById(userStatus.getId()).isPresent()) {
      throw new IllegalArgumentException("해당 User와 관련된 UserStatus가 이미 존재합니다.");
    }

    */
    repository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional(readOnly = true)
  public UserStatusResponse findUserStatusById(UUID userStatusId) {
    UserStatus userStatus = getUserStatusOrThrow(userStatusId);

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional(readOnly = true)
  public List<UserStatusResponse> findAllUserStatus() {
    return repository.findAll().stream().map(userStatusMapper::toDto).toList();
  }

  @Transactional
  public UserStatusResponse updateUserStatus(UUID userStatusId,
      UserStatusUpdateRequest request) {//파라미터값 value만 적용
    UserStatus userStatus = getUserStatusOrThrow(userStatusId);
    userStatus.updateLastOnline(request.newLastActiveAt());
    userStatus.updateUserStatus(request.userStatus());

    repository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  public UserStatusResponse updateUserStatusByUserId(UUID userId,
      UserStatusUpdateRequest request) { //수정 프로퍼티 미정
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저가 없습니다."));
    UserStatus userStatus = getUserStatusOrThrow(user.getUserStatus().getId());
    // 프론트엔드가 보내는 마지막 활동 시각으로 온라인 상태를 갱신한다.
    Instant lastActiveAt =
        request.newLastActiveAt() != null ? request.newLastActiveAt() : Instant.now();
    userStatus.updateLastOnline(lastActiveAt);
    repository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  public void deleteUserStatus(UUID userStatusId) {
    repository.deleteById(userStatusId);
    System.out.println("userstatus 삭제됨");
  }

  private UserStatus getUserStatusOrThrow(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 UserStatus가 없습니다."));
  }

}
