package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    void createUser(User user);
    User findUserById(UUID id);
    List<User> findAllUser();
    void updateUser(User user);
    void deleteUser(UUID id);
    boolean isExistEmail(User user);
    boolean isExistPhoneNumber(User user);
}
