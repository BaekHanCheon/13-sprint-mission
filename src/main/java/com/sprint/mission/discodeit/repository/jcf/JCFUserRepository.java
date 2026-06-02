package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userData = new HashMap<>();

    @Override
    public void createUser(User user) {
        userData.put(user.getId(), user);
    }

    @Override
    public User readUser(UUID id) {
        return userData.get(id);
    }

    @Override
    public void readAllUser() {
        userData.values().stream().toList().forEach(System.out::println);
    }

    @Override
    public void modifyUser(User user) {
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