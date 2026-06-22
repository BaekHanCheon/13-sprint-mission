package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/createPublicChannel")
    public ResponseEntity<ChannelResponse> createPublicChannel(@RequestBody ChannelPublicCreateRequest request) {

        return ResponseEntity.ok(channelService.createPublicChannel(request));
    }

    @PostMapping("/createPrivateChannel")
    public ResponseEntity<ChannelResponse> createPrivateChannel(@RequestBody ChannelPrivateCreateRequest request) {

        return ResponseEntity.ok(channelService.createPrivateChannel(request));
    }

    @PatchMapping("/updateChannel")
    public ResponseEntity<ChannelResponse> updateChannel(@RequestBody ChannelUpdateRequest request) {

        return ResponseEntity.ok(channelService.updateChannel(request));
    }

    @DeleteMapping("/deleteChannel/{channelId}")
    public String deleteChannel(@PathVariable("channelId") UUID channelId) {
        channelService.deleteChannel(channelId);

        return "channel deleted";
    }

    @GetMapping("/findAllChannelByUserId/{channelId}")
    public ResponseEntity<List<ChannelResponse>> findChannelByUserId(@PathVariable("channelId") UUID userId) {

        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

}
//채널 관리
//[ ] 공개 채널을 생성할 수 있다.
//[ ] 비공개 채널을 생성할 수 있다.
//[ ] 공개 채널의 정보를 수정할 수 있다.
//        [ ] 채널을 삭제할 수 있다.
//        [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.