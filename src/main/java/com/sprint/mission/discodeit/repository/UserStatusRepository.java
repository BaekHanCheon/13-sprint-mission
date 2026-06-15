package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    public void createUserStatus(UserStatus userStatus);
    public Optional<UserStatus> findUserStatusById(UUID id);
    public List<UserStatus> findAllUserStatus();
    public void deleteUserStatus(UUID id);
    public void updateUserStatus(UserStatus userStatus);
}
