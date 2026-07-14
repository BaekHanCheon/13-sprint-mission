package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "ReadStatus", description = "메시지 수신 정보 관리 API")
public interface ReadStatusApi {

  @Operation(summary = "메시지 수신 정보 생성", description = "특정 채널의 메시지 수신 정보를 생성합니다.")
  ResponseEntity<ReadStatusResponse> createReadStatus(ReadStatusCreateRequest request);

  @Operation(summary = "메시지 수신 정보 수정", description = "특정 채널의 메시지 수신 정보를 수정합니다.")
  ResponseEntity<ReadStatusResponse> updateReadStatus(
      @Parameter(description = "수정할 수신 정보 ID") UUID readStatusId,
      ReadStatusUpdateRequest request
  );

  @Operation(summary = "사용자 수신 정보 조회", description = "특정 사용자의 메시지 수신 정보 목록을 조회합니다.")
  ResponseEntity<List<ReadStatusResponse>> findAllReadStatusByUserId(
      @Parameter(description = "사용자 ID") UUID userId
  );
}
