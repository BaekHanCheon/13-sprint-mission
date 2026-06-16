package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
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
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class BasicUserService implements UserService {
    private final UserRepository repository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        validateDuplicateUser(request);

        User user = request.toEntity();

        //프로필 생성
        if (request.profileImageRequest() != null ) {
            BinaryContentCreateRequest profileImageRequest = request.profileImageRequest();
            BinaryContent binaryContent = new BinaryContent(profileImageRequest.filePath().toString(),user.getId(),null);
            binaryContentRepository.createBinaryContent(binaryContent);
            user.updateProfileId(binaryContent.getId());
            binaryContentRepository.saveFile(profileImageRequest.filePath());

        } else {
                log.info("메시지에 첨부파일이 없습니다.");
        }

        //userStatus 생성
        UserStatus userStatus = new UserStatus(user.getId(),null);
        user.updateUserStatusId(userStatus.getId());
        userStatusRepository.createUserStatus(userStatus);

        repository.createUser(user);
        log.info("유저 생성 - {}", user.getUserName());
        return UserResponse.from(user);
    }

    @Override
    public UserResponse findUserById(UUID userId) {
        User user = getUserOrThrow(userId);
        UserStatus userStatus = userStatusRepository.findUserStatusById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 userStatus"));
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
    public void updateUser(UUID userId, String property, String value) {
        User user = getUserOrThrow(userId);

        switch (property.toUpperCase()) {
            case "PASSWORD": user.updatePassword(value); System.out.println("비밀번호 수정"); break;
            case "EMAIL": user.updateEmail(value); System.out.println("이메일 수정"); break;
            case "USERNAME": user.updateUserName(value); System.out.println("유저이름 수정"); break;
        }

        user.updateUpdatedAt();
        repository.updateUser(user);
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

    private void validateDuplicateUser(UserCreateRequest request) {
        if (repository.isExistEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (repository.isExistPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
        }
        if (repository.isExistUsername(request.userName())) {
            throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
        }
    }
}