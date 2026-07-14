package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class readStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @Override
  @PostMapping
  public ResponseEntity<ReadStatusResponse> createReadStatus(
      @RequestBody ReadStatusCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.createReadStatus(request));
  }

  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusResponse> updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.updateReadStatus(readStatusId, request));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatusResponse>> findAllReadStatusByUserId(
      @RequestParam("userId") UUID userId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.findAllReadStatusByUserId(userId));
  }
}
