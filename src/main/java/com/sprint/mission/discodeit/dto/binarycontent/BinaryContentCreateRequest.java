package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;

public record BinaryContentCreateRequest(
        Path filePath
){ }
