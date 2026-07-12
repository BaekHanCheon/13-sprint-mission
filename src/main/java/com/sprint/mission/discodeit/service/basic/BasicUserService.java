package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository repository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserResponse createUser(UserCreateRequest request, MultipartFile profile) {

    String userName = request.username();
    String email = request.email();
    String phoneNumber = request.phoneNumber();

    validateDuplicateUser(userName, email, phoneNumber);

    User user = request.toEntity();

    //프로필 생성
    if (profile != null && !profile.isEmpty()) {
      String savedFileName = profile.getOriginalFilename();
      Long size = profile.getSize();
      String contentType = profile.getContentType();

      BinaryContent binaryContent = new BinaryContent(savedFileName, size, contentType, user, null);
      binaryContentRepository.save(binaryContent);
      user.updateProfile(binaryContent);

      try {
        byte[] bytes = profile.getBytes();
        binaryContentStorage.put(binaryContent.getId(), bytes);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      log.info("유저 프로필에 첨부파일이 없습니다.");
    }

    //userStatus 생성
    UserStatus userStatus = new UserStatus(user);
    user.updateStatus(userStatus);
    userStatusRepository.save(userStatus);

    // 회원가입 직후 바로 로그인 상태가 되도록 온라인으로 설정 (로그인 흐름과 동일)
    user.updateOnline(true);

    repository.save(user);
    log.info("유저 생성 - {}", user.getUsername());
    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findUserById(UUID userId) {
    User user = getUserOrThrow(userId);
    UserStatus userStatus = userStatusRepository.findById(user.getUserStatus().getId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userStatus"));
    user.updateOnline(userStatus.isOnline());
    log.info("유저 조회 - {}", user.getUsername());
    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> findAllUser() {
    log.info("전체 유저 조회 : ");

    return repository.findAll().stream().map(userMapper::toDto).toList();
  }

  @Override
  @Transactional
  public UserResponse updateUser(UUID userId, UserUpdateRequest request, MultipartFile profile) {

    User userToUpdate = getUserOrThrow(userId);

    String newUsername = request.newUsername();
    String newEmail = request.newEmail();
    String newPhoneNumber = request.phoneNumber();
    String newPassword = request.newPassword();

    validateDuplicateUserForUpdate(userId, newUsername, newEmail, newPhoneNumber);

    if (newUsername != null) {
      userToUpdate.updateUserName(newUsername);
    }
    if (newEmail != null) {
      userToUpdate.updateEmail(newEmail);
    }
    if (newPhoneNumber != null) {
      userToUpdate.updatePhoneNumber(newPhoneNumber);
    }
    if (newPassword != null) {
      userToUpdate.updatePassword(newPassword);
    }

    if (profile != null && !profile.isEmpty()) {
      String savedFileName = profile.getOriginalFilename();
      Long size = profile.getSize();
      String contentType = profile.getContentType();

      BinaryContent binaryContent = new BinaryContent(savedFileName, size, contentType,
          userToUpdate, null);
      binaryContentRepository.save(binaryContent);
      userToUpdate.updateProfile(binaryContent);

      try {
        byte[] bytes = profile.getBytes();
        binaryContentStorage.put(binaryContent.getId(), bytes);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      log.info("유저 프로필에 첨부파일이 없습니다.");
    }

    repository.save(userToUpdate);

    log.info("유저 수정 - {}", userToUpdate.getUsername());
    return userMapper.toDto(userToUpdate);
  }

  @Override
  @Transactional
  public void deleteUser(UUID userid) {
    User user = getUserOrThrow(userid);
    userStatusRepository.deleteById(user.getUserStatus().getId());
    if (user.getProfile().getId() != null) {
      binaryContentRepository.deleteById(user.getProfile().getId());
    }
    repository.deleteById(userid);

    System.out.println("유저 삭제");
  }


  private User getUserOrThrow(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
  }

  private void validateDuplicateUser(String userName, String email, String phoneNumber) {
    if (email != null && repository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
    }
    // 전화번호는 선택 입력값이므로, 값이 있을 때만 중복 검사한다.
    if (phoneNumber != null && !phoneNumber.isBlank() && repository.existsByPhoneNumber(
        phoneNumber)) {
      throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
    }
    if (userName != null && repository.existsByUsername(userName)) {
      throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
    }
  }

  private void validateDuplicateUserForUpdate(UUID currentUserId, String userName, String email,
      String phoneNumber) {
    repository.findAll().stream()
        .filter(user -> !user.getId().equals(currentUserId))
        .forEach(user -> {
          if (email != null && Objects.equals(user.getEmail(), email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
          }
          if (phoneNumber != null && !phoneNumber.isBlank()
              && Objects.equals(user.getPhoneNumber(), phoneNumber)) {
            throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
          }
          if (userName != null && Objects.equals(user.getUsername(), userName)) {
            throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
          }
        });
  }

}
