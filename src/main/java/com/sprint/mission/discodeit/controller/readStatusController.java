package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class readStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping("/createReadStatus")
    public ResponseEntity<ReadStatusResponse> createReadStatus(@RequestBody ReadStatusCreateRequest request) {

        return ResponseEntity.ok(readStatusService.createReadStatus(request));
    }

    @PatchMapping("/updateReadStatus")
    public ResponseEntity<ReadStatusResponse> updateReadStatus(@RequestBody ReadStatusUpdateRequest request) {

        return ResponseEntity.ok(readStatusService.updateReadStatus(request));
    }

    @GetMapping("/findAllReadStatusByUserId")
    public List<ReadStatusResponse> findAllReadStatusByUserId(@RequestParam("id") UUID userId) {

        return readStatusService.findAllReadStatusByUserId(userId);
    }
}
//[ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
//        [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
//        [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.