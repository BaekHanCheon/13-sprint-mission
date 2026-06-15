package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);
    UserResponse findUserById(UUID id);
    List<UserResponse> findAllUser();
    void updateUser(UUID id, String property, String value);
    void deleteUser(UUID id);
}
