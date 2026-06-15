package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path binaryPath = Path.of("data/ReadStatuses.ser");

    @Override
    public void createReadStatus(ReadStatus readStatus) {
        Map<UUID, ReadStatus> data = load();
        data.put(readStatus.getId(), readStatus);
        save(data);
    }

    @Override
    public Optional<ReadStatus> findReadStatusById(UUID id) {
        ReadStatus readStatus = load().get(id);
        if (readStatus == null) {
            throw new IllegalArgumentException("ReadStatus 를 찾을 수 없습니다.");
        }
        return Optional.ofNullable(readStatus);
    }

    @Override
    public List<ReadStatus> findAllReadStatusByUserId(UUID userId) {
        return load().values().stream().sorted(Comparator.comparing(ReadStatus::getCreatedAt)).toList();
    }

    @Override
    public void deleteReadStatus(UUID id) {
        Map<UUID, ReadStatus> data = load();
        if (data.get(id) == null) {
            throw new NullPointerException("삭제하고자 하는 id의 데이터가 없습니다.");
        }
        data.remove(id);
        save(data);
    }

    @Override
    public void updateReadStatus(ReadStatus readStatus) {
        Map<UUID, ReadStatus> data = load();
        data.put(readStatus.getId(), readStatus);
        save(data);
    }

    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return load().values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(ReadStatus::getCreatedAt))
                .toList();
    }

    private void save(Map<UUID, ReadStatus> storage) {
        Path parent = binaryPath.getParent();
        if (parent != null) {
            try { Files.createDirectories(parent); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(binaryPath)))) {
            oos.writeObject(new HashMap<>(storage));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private Map<UUID, ReadStatus> load() {
        if (!Files.exists(binaryPath)) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(binaryPath)))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
    }
}
