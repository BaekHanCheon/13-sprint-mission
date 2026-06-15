package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    public void createReadStatus(ReadStatus readStatus);
    public Optional<ReadStatus> findReadStatusById(UUID id);
    public List<ReadStatus> findAllReadStatusByUserId(UUID userId);
    public void deleteReadStatus(UUID id);
    public void updateReadStatus(ReadStatus readStatus);
    List<ReadStatus> findAllByChannelId(UUID channelId);
}
