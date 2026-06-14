package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository repository;
    private final UserRepository userRepository;

    public UserStatusResponse createUserStatus(UserStatusCreateRequest request){
        UserStatus userStatus = request.toEntity();
        userRepository.findUserById(userStatus.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 User가 없습니다."));

        if (repository.findUserStatusById(userStatus.getId()).isPresent()) {
            throw new IllegalArgumentException("해당 User와 관련된 UserStatus가 이미 존재합니다.");
        }

        repository.createUserStatus(userStatus);
        return UserStatusResponse.from(userStatus);
    }

    public UserStatusResponse findUserStatusById(UUID userStatusId){
        UserStatus userStatus = getUserStatusOrThrow(userStatusId);

        return UserStatusResponse.from(userStatus);
    }

    public List<UserStatusResponse> findAllUserStatus(){
        return repository.findAllUserStatus().stream().map(UserStatusResponse::from).toList();
    }


    public void updateUserStatus(UserStatusUpdateRequest request){//파라미터값 value만 적용
        UserStatus userStatus = getUserStatusOrThrow(request.id());
        //userStatus.updateLastUserAt(request.lastUserAt());
        userStatus.updateUpdatedAt();

        repository.updateUserStatus(userStatus);
    }

    public void updateUserByUserId(UUID userid){ //수정 프로퍼티 미정
        User user = userRepository.findUserById(userid).orElseThrow(() -> new NoSuchElementException("유저가 없습니다."));

        user.updateUpdatedAt();
        userRepository.updateUser(user);
    }

    public void deleteUserStatus(UUID userStatusId){
        repository.deleteUserStatus(userStatusId);
        System.out.println("userstatus 삭제됨");}

    private UserStatus getUserStatusOrThrow(UUID id){
        return repository.findUserStatusById(id).orElseThrow(() -> new NoSuchElementException("해당 UserStatus가 없습니다."));
    }

}
