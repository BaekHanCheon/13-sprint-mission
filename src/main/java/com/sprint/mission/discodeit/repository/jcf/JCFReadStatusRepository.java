package com.sprint.mission.discodeit.repository.jcf;

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
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusData = new HashMap<>();

    @Override
    public void createReadStatus(ReadStatus readStatus) {

        readStatusData.put(readStatus.getId(), readStatus);

    }

    @Override
    public Optional<ReadStatus> findReadStatusById(UUID readStatusId) {

        if (readStatusData == null) {
            throw new IllegalArgumentException("ReadStatus 를 찾을 수 없습니다.");
        }
        return Optional.ofNullable(readStatusData.get(readStatusId));
    }

    @Override
    public List<ReadStatus> findAllReadStatusByUserId(UUID userId) {
        return readStatusData.values().stream().sorted(Comparator.comparing(ReadStatus::getCreatedAt)).toList();
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {

        if (readStatusData.get(readStatusId) == null) {
            throw new NullPointerException("삭제하고자 하는 id의 데이터가 없습니다.");
        }
        readStatusData.remove(readStatusId);

    }

    @Override
    public void updateReadStatus(ReadStatus readStatus) {

        readStatusData.put(readStatus.getId(), readStatus);
    }

    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusData.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(ReadStatus::getCreatedAt))
                .toList();
    }

}
