package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userData = new HashMap<>();

    public boolean isExistEmail(String email) {
        return userData.values().stream().anyMatch(u -> Objects.equals(u.getEmail(), email));
    }

    public boolean isExistPhoneNumber(String phoneNumber) {
        return userData.values().stream().anyMatch(u -> Objects.equals(u.getPhoneNumber(), phoneNumber));
    }

    @Override
    public boolean isExistUsername(String username) {
        return userData.values().stream().anyMatch(u -> Objects.equals(u.getUserName(), username));
    }

    @Override
    public void createUser(User user) {
        userData.put(user.getId(), user);
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        User user = userData.get(id);
        return Optional.ofNullable(user);
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
}