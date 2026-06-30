package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

  private final static Path BINARY_PATH = Path.of("data/Users.ser");

  public boolean isExistEmail(String email) {
    return load().values().stream().anyMatch(u -> Objects.equals(u.getEmail(), email));
  }

  public boolean isExistPhoneNumber(String phoneNumber) {
    return load().values().stream().anyMatch(u -> Objects.equals(u.getPhoneNumber(), phoneNumber));
  }

  @Override
  public boolean isExistUsername(String username) {
    return load().values().stream().anyMatch(u -> Objects.equals(u.getUserName(), username));
  }

  @Override
  public void createUser(User user) {
    Map<UUID, User> data = load();
    data.put(user.getId(), user);
    save(data);
  }

  @Override
  public Optional<User> findUserById(UUID id) {
    User user = load().get(id);
    return Optional.ofNullable(user);
  }

  @Override
  public List<User> findAllUser() {
    return load().values().stream().sorted(Comparator.comparing(User::getCreatedAt)).toList();
  }

  @Override
  public void updateUser(User user) {
    Map<UUID, User> data = load();
    data.put(user.getId(), user);
    save(data);
  }

  @Override
  public void deleteUser(UUID id) {
    Map<UUID, User> data = load();
    data.remove(id);
    save(data);
  }

  private void save(Map<UUID, User> storage) {
    Path parent = BINARY_PATH.getParent();
    if (parent != null) {
      try {
        Files.createDirectories(parent);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new BufferedOutputStream(Files.newOutputStream(BINARY_PATH)))) {
      oos.writeObject(new HashMap<>(storage));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<UUID, User> load() {
      if (!Files.exists(BINARY_PATH)) {
          return new HashMap<>();
      }
    try (ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(Files.newInputStream(BINARY_PATH)))) {
      return (Map<UUID, User>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}