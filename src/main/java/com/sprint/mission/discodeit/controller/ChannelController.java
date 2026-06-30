package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "채널 관리 API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "공개 채널 생성", description = "공개 채널을 생성합니다.")
    @PostMapping("/public")
    public ResponseEntity<ChannelResponse> createPublicChannel(@RequestBody ChannelPublicCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPublicChannel(request));
    }

    @Operation(summary = "비공개 채널 생성", description = "참여자를 지정하여 비공개 채널을 생성합니다.")
    @PostMapping("/private")
    public ResponseEntity<ChannelResponse> createPrivateChannel(@RequestBody ChannelPrivateCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPrivateChannel(request));
    }

    @Operation(summary = "채널 정보 수정", description = "공개 채널의 정보를 수정합니다.")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> updateChannel(
            @Parameter(description = "수정할 채널 ID") @PathVariable UUID channelId,
            @RequestBody ChannelUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(channelService.updateChannel(channelId, request));
    }

    @Operation(summary = "채널 삭제", description = "채널을 삭제합니다.")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> deleteChannel(
            @Parameter(description = "삭제할 채널 ID") @PathVariable("channelId") UUID channelId) {
        channelService.deleteChannel(channelId);

        return ResponseEntity.status(HttpStatus.OK).body("channel deleted");
    }

    @Operation(summary = "사용자 채널 목록 조회", description = "특정 사용자가 볼 수 있는 모든 채널 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ChannelResponse>> findChannelByUserId(
            @Parameter(description = "사용자 ID") @RequestParam("userId") UUID userId) {

        return ResponseEntity.status(HttpStatus.OK).body(channelService.findAllByUserId(userId));
    }

}
//채널 관리
//[ ] 공개 채널을 생성할 수 있다.
//[ ] 비공개 채널을 생성할 수 있다.
//[ ] 공개 채널의 정보를 수정할 수 있다.
//        [ ] 채널을 삭제할 수 있다.
//        [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.