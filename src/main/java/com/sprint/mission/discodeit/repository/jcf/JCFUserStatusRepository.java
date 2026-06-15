package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusData = new HashMap<>();


    @Override
    public void createUserStatus(UserStatus userStatus) {

        userStatusData.put(userStatus.getId(), userStatus);
    }

    @Override
    public Optional<UserStatus> findUserStatusById(UUID userStatusId) {
        UserStatus userStatus = userStatusData.get(userStatusId);
        if (userStatus == null) {
            throw new IllegalArgumentException("userStatus 를 찾을 수 없습니다.");
        }
        return Optional.ofNullable(userStatus);
    }

    @Override
    public List<UserStatus> findAllUserStatus() {
        return userStatusData.values().stream().sorted(Comparator.comparing(UserStatus::getCreatedAt)).toList();
    }

    @Override
    public void updateUserStatus(UserStatus userStatus) {

        userStatusData.put(userStatus.getId(), userStatus);
    }

    @Override
    public void deleteUserStatus(UUID userId) {

        if (userStatusData.get(userId) == null) {
            throw new NullPointerException("삭제하고자 하는 id의 데이터가 없습니다.");
        }
        userStatusData.remove(userId);
    }

}
