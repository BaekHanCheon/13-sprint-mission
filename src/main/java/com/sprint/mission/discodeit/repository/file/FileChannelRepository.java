package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@Repository
public class FileChannelRepository implements ChannelRepository {
    private final Path binaryPath = Path.of("data/channels.ser");

    private void save(Map<UUID, Channel> storage) {
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

    private Map<UUID, Channel> load() {
        if (!Files.exists(binaryPath)) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(binaryPath)))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    @Override
    public void createChannel(Channel channel) {
        Map<UUID, Channel> data = load();
        data.put(channel.getId(), channel);
        save(data);
    }

    @Override
    public Channel findChannelById(UUID id) {
        Channel channel = load().get(id);
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
        }
        return channel;
    }

    @Override
    public List<Channel> findAllChannel() {
        return load().values().stream().sorted(Comparator.comparing(Channel::getCreatedAt)).toList();
    }

    @Override
    public void updateChannel(Channel channel) {
        Map<UUID, Channel> data = load();
        data.put(channel.getId(), channel);
        save(data);
        log.info(data.toString());
    }

    @Override
    public void deleteChannel(UUID id) {
        Map<UUID, Channel> data = load();
        data.remove(id);
        save(data);
    }
}