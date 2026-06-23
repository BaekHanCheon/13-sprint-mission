package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);
    UserResponse findUserById(UUID id);
    List<UserResponse> findAllUser();
    UserResponse updateUser(UserUpdateRequest request);
    void deleteUser(UUID id);
}
