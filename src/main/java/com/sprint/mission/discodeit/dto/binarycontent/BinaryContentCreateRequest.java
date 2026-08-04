package com.sprint.mission.discodeit.dto.binarycontent;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateRequest(
    MultipartFile file
) {

}
