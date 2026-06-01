package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface UserRepository {

    void createUser(User user);
    User readUser(UUID id);
    void readAllUser();
    void modifyUser(User user);
    void deleteUser(UUID id);
    boolean isExistEmail(User user);
    boolean isExistPhoneNumber(User user);
}
