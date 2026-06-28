package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "메시지 수신 정보 관리 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class readStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "메시지 수신 정보 생성", description = "특정 채널의 메시지 수신 정보를 생성합니다.")
  @PostMapping
  public ResponseEntity<ReadStatusResponse> createReadStatus(
      @RequestBody ReadStatusCreateRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.createReadStatus(request));
  }

  @Operation(summary = "메시지 수신 정보 수정", description = "특정 채널의 메시지 수신 정보를 수정합니다.")
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusResponse> updateReadStatus(
      @Parameter(description = "수정할 수신 정보 ID") @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.updateReadStatus(readStatusId, request));
  }

  @Operation(summary = "사용자 수신 정보 조회", description = "특정 사용자의 메시지 수신 정보 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<ReadStatusResponse>> findAllReadStatusByUserId(
      @Parameter(description = "사용자 ID") @RequestParam("userId") UUID userId) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.findAllReadStatusByUserId(userId));
  }
}
//[ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
//        [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
//        [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.