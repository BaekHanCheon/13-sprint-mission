package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    public void createBinaryContent(BinaryContent binaryContent);
    public Optional<BinaryContent> findBinaryContentById(UUID id);
    public List<BinaryContent> findAllBinaryContentByIdIn(List<UUID> idList);
    public void deleteBinaryContent(UUID id);

    public String saveFile(Path sourcePath);


}
