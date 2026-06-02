package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;


public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userData = new HashMap<>();

    @Override
    public void createUser(User user) {
        userData.put(user.getId(), user);
    }

    @Override
    public User findUserById(UUID id) {
        User user = userData.get(id);
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }
        return user;
    }

    @Override
    public List<User> findAllUser() {
        return userData.values().stream().sorted(Comparator.comparing(User::getCreatedAt)).toList();
    }

    @Override
    public void updateUser(User user) {
        userData.put(user.getId(), user);
    }

    @Override
    public void deleteUser(UUID id) {
        userData.remove(id);
    }

    public boolean isExistEmail(User user) {
        return userData.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()));
    }

    public boolean isExistPhoneNumber(User user) {
        return userData.values().stream().anyMatch(u -> u.getPhoneNumber().equals(user.getPhoneNumber()));
    }
}