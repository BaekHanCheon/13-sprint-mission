package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void createUser(User user);
    Optional<User> findUserById(UUID id);
    List<User> findAllUser();
    void updateUser(User user);
    void deleteUser(UUID id);
    boolean isExistEmail(String email);
    boolean isExistPhoneNumber(String phoneNumber);
    boolean isExistUsername(String username);
}
