package com.sprint.mission.discodeit.repository.file;

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
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {

  private final static Path BINARY_PATH = Path.of("data/Userstatuses.ser");


  @Override
  public void createUserStatus(UserStatus userStatus) {
    Map<UUID, UserStatus> data = load();
    data.put(userStatus.getId(), userStatus);
    save(data);
  }

  @Override
  public Optional<UserStatus> findUserStatusById(UUID id) {
    UserStatus userStatus = load().get(id);
    if (userStatus == null) {
      throw new IllegalArgumentException("userStatus 를 찾을 수 없습니다.");
    }
    return Optional.ofNullable(userStatus);
  }

  @Override
  public List<UserStatus> findAllUserStatus() {
    return load().values().stream().sorted(Comparator.comparing(UserStatus::getCreatedAt)).toList();
  }

  @Override
  public void updateUserStatus(UserStatus userStatus) {
    Map<UUID, UserStatus> data = load();
    data.put(userStatus.getId(), userStatus);
    save(data);
  }

  @Override
  public void deleteUserStatus(UUID id) {
    Map<UUID, UserStatus> data = load();
    if (data.get(id) == null) {
      throw new NullPointerException("삭제하고자 하는 id의 데이터가 없습니다.");
    }
    data.remove(id);
    save(data);
  }

  private void save(Map<UUID, UserStatus> storage) {
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

  private Map<UUID, UserStatus> load() {
      if (!Files.exists(BINARY_PATH)) {
          return new HashMap<>();
      }
    try (ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(Files.newInputStream(BINARY_PATH)))) {
      return (Map<UUID, UserStatus>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
