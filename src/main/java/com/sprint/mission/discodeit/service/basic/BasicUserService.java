package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

  @Override
  public UserResponse createUser(UserCreateRequest request, MultipartFile profile) {

    String userName = request.username();
    String email = request.email();
    String phoneNumber = request.phoneNumber();

    validateDuplicateUser(userName, email, phoneNumber);

    User user = request.toEntity();

    //프로필 생성
    if (profile != null && !profile.isEmpty()) {
      String savedFileName = binaryContentRepository.saveFile(profile);
      BinaryContent binaryContent = new BinaryContent(savedFileName, user.getId(), null);
      binaryContentRepository.createBinaryContent(binaryContent);
      user.updateProfileId(binaryContent.getId());

    } else {
      log.info("유저 프로필에 첨부파일이 없습니다.");
    }

    //userStatus 생성
    UserStatus userStatus = new UserStatus(user.getId());
    user.updateUserStatusId(userStatus.getId());
    userStatusRepository.createUserStatus(userStatus);

    // 회원가입 직후 바로 로그인 상태가 되도록 온라인으로 설정 (로그인 흐름과 동일)
    user.updateOnline(true);

    repository.createUser(user);
    log.info("유저 생성 - {}", user.getUserName());
    return UserResponse.from(user);
  }

  @Override
  public UserResponse findUserById(UUID userId) {
    User user = getUserOrThrow(userId);
    UserStatus userStatus = userStatusRepository.findUserStatusById(user.getUserStatusId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userStatus"));
    user.updateOnline(userStatus.isOnline());
    log.info("유저 조회 - {}", user.getUserName());
    return UserResponse.from(user);
  }

  @Override
  public List<UserResponse> findAllUser() {
    log.info("전체 유저 조회 : ");

    return repository.findAllUser().stream().map(UserResponse::from).toList();
  }

  @Override
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
      UUID profileId = userToUpdate.getProfileId();
      if (profileId != null) {
        binaryContentRepository.deleteBinaryContent(profileId);
      }

      String savedFileName = binaryContentRepository.saveFile(profile);
      BinaryContent binaryContent = new BinaryContent(savedFileName, userId, null);

      binaryContentRepository.createBinaryContent(binaryContent);
      userToUpdate.updateProfileId(binaryContent.getId());

    } else {
      log.info("유저 프로필에 첨부파일이 없습니다.");
    }

    repository.updateUser(userToUpdate);
    userToUpdate.updateUpdatedAt();
    log.info("유저 수정 - {}", userToUpdate.getUserName());
    return UserResponse.from(userToUpdate);
  }

  @Override
  public void deleteUser(UUID userid) {
    User user = getUserOrThrow(userid);
    userStatusRepository.deleteUserStatus(user.getUserStatusId());
    if (user.getProfileId() != null) {
      binaryContentRepository.deleteBinaryContent(user.getProfileId());
    }
    repository.deleteUser(userid);

    System.out.println("유저 삭제");
  }


  private User getUserOrThrow(UUID id) {
    return repository.findUserById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
  }

  private void validateDuplicateUser(String userName, String email, String phoneNumber) {
    if (email != null && repository.isExistEmail(email)) {
      throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
    }
    // 전화번호는 선택 입력값이므로, 값이 있을 때만 중복 검사한다.
    if (phoneNumber != null && !phoneNumber.isBlank() && repository.isExistPhoneNumber(
        phoneNumber)) {
      throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
    }
    if (userName != null && repository.isExistUsername(userName)) {
      throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
    }
  }

  private void validateDuplicateUserForUpdate(UUID currentUserId, String userName, String email,
      String phoneNumber) {
    repository.findAllUser().stream()
        .filter(user -> !user.getId().equals(currentUserId))
        .forEach(user -> {
          if (email != null && Objects.equals(user.getEmail(), email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
          }
          if (phoneNumber != null && !phoneNumber.isBlank()
              && Objects.equals(user.getPhoneNumber(), phoneNumber)) {
            throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
          }
          if (userName != null && Objects.equals(user.getUserName(), userName)) {
            throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
          }
        });
  }

}