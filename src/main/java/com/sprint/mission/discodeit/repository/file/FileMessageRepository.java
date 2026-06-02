package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path binaryPath = Path.of("data/Messages.ser");

    private void save(Map<UUID, Message> storage) {
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

    private Map<UUID, Message> load() {
        if (!Files.exists(binaryPath)) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(binaryPath)))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    @Override
    public void createMessage(Message message, User user, Channel channel) {
        Map<UUID, Message> data = load();
        data.put(message.getId(), message);
        save(data);
    }

    @Override
    public Message findMessageById(UUID id) {
        Message message = load().get(id);
        if (message == null) {
            throw new IllegalArgumentException("메세지를 찾을 수 없습니다.");
        }
        return message;
    }

    @Override
    public List<Message> findAllMessage() {
        return load().values().stream().sorted(Comparator.comparing(Message::getCreatedAt)).toList();
    }

    @Override
    public void updateMessage(Message message) {
        Map<UUID, Message> data = load();
        data.put(message.getId(), message);
        save(data);
    }

    @Override
    public void deleteMessage(UUID id) {
        Map<UUID, Message> data = load();
        data.remove(id);
        save(data);
    }
}