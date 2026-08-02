package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MessageUpdateRequest(
        @NotBlank(message = "메시지 내용은 필수입니다.")
        @Size(max = 255, message = "메시지 내용은 255자 이하여야 합니다.")
        String newContent
) {

}
