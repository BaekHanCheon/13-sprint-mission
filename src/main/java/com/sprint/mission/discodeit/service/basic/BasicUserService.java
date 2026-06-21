package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class BasicUserService implements UserService {
    private final UserRepository repository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        String userName = request.userName();
        String email = request.email();
        String phoneNumber = request.phoneNumber();

        validateDuplicateUser(userName,email,phoneNumber);

        User user = request.toEntity();

        //프로필 생성
        if (request.profileImageRequest() != null ) {
            BinaryContentCreateRequest profileImageRequest = request.profileImageRequest();
            String savedFileName = binaryContentRepository.saveFile(profileImageRequest.filePath());
            BinaryContent binaryContent = new BinaryContent(savedFileName, user.getId(), null);
            binaryContentRepository.createBinaryContent(binaryContent);
            user.updateProfileId(binaryContent.getId());

        } else {
                log.info("메시지에 첨부파일이 없습니다.");
        }

        //userStatus 생성
        UserStatus userStatus = new UserStatus(user.getId());
        user.updateUserStatusId(userStatus.getId());
        userStatusRepository.createUserStatus(userStatus);

        repository.createUser(user);
        log.info("유저 생성 - {}", user.getUserName());
        return UserResponse.from(user);
    }

    @Override
    public UserResponse findUserById(UUID userId) {
        User user = getUserOrThrow(userId);
        UserStatus userStatus = userStatusRepository.findUserStatusById(user.getUserStatusId()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 userStatus"));
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
    public UserResponse updateUser(UserUpdateRequest request) {

        User userToUpdate = getUserOrThrow(request.id());

        String newUsername = request.userName();
        String newEmail = request.email();
        String newPhoneNumber = request.phoneNumber();
        String newPassword = request.password();

        validateDuplicateUserForUpdate(request.id(), newUsername, newEmail, newPhoneNumber);

        userToUpdate.updateUserName(newUsername);
        userToUpdate.updateEmail(newEmail);
        userToUpdate.updatePhoneNumber(newPhoneNumber);
        userToUpdate.updatePassword(newPassword);

        if (request.profileImageRequest() != null ) {
            BinaryContentCreateRequest profileImageRequest = request.profileImageRequest();
            UUID profileId = userToUpdate.getProfileId();
            if(profileId != null){
                binaryContentRepository.deleteBinaryContent(profileId);
            }

            String savedFileName = binaryContentRepository.saveFile(profileImageRequest.filePath());
            BinaryContent binaryContent = new BinaryContent(savedFileName, request.id(), null);

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
        if(user.getProfileId() != null) {
            binaryContentRepository.deleteBinaryContent(user.getProfileId());
        }
        repository.deleteUser(userid);

        System.out.println("유저 삭제");
    }


    private User getUserOrThrow(UUID id) {
        return repository.findUserById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    }

    private void validateDuplicateUser(String userName,String email,String phoneNumber) {
        if (repository.isExistEmail(email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (repository.isExistPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
        }
        if (repository.isExistUsername(userName)) {
            throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
        }
    }

    private void validateDuplicateUserForUpdate(UUID currentUserId, String userName, String email, String phoneNumber) {
        repository.findAllUser().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .forEach(user -> {
                    if (user.getEmail().equals(email)) {
                        throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
                    }
                    if (user.getPhoneNumber().equals(phoneNumber)) {
                        throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
                    }
                    if (user.getUserName().equals(userName)) {
                        throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
                    }
                });
    }

}