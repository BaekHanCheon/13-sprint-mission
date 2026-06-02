package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {

    void createUser(User user);
    void findUserById(UUID id);
    void findAllUser();
    void updateUser(UUID id, String property, String value);
    void deleteUser(UUID id);
}
