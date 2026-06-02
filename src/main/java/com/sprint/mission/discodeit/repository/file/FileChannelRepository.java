package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
    public Channel readChannel(UUID id) {
        return load().get(id);
    }

    @Override
    public void readAllChannel() {
        load().values().forEach(System.out::println);
    }

    @Override
    public void modifyChannel(Channel channel) {
        Map<UUID, Channel> data = load();
        data.put(channel.getId(), channel);
        save(data);
    }

    @Override
    public void deleteChannel(UUID id) {
        Map<UUID, Channel> data = load();
        data.remove(id);
        save(data);
    }
}