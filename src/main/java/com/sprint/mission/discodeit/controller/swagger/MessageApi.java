package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "메시지 관리 API")
public interface MessageApi {

  @Operation(summary = "메시지 전송")
  ResponseEntity<MessageResponse> createMessage(
      MessageCreateRequest request,
      List<MultipartFile> attachments);

  @Operation(summary = "메시지 수정")
  ResponseEntity<MessageResponse> updateMessage(
      @Parameter(description = "메시지 ID") UUID messageId,
      MessageUpdateRequest request);

  @Operation(summary = "메시지 삭제")
  ResponseEntity<Void> deleteMessage(
      @Parameter(description = "메시지 ID") UUID messageId);

  @Operation(summary = "채널 메시지 커서 조회", description = "최신 메시지부터 50개씩 조회합니다.")
  ResponseEntity<PageResponse<MessageResponse>> findMessageByChannelId(
      @Parameter(description = "채널 ID") UUID channelId,
      @Parameter(description = "이전 응답의 커서") Instant cursor,
      @Parameter(description = "페이지 크기와 정렬 정보") Pageable pageable);
}
