package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @PostMapping("/createMessage")
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageCreateRequest request) {

        return ResponseEntity.ok(messageService.createMessage(request));
    }

    @PatchMapping("/updateMessage")
    public ResponseEntity<MessageResponse> updateMessage(@RequestBody MessageUpdateRequest request) {

        return ResponseEntity.ok(messageService.updateMessage(request));
    }

    @DeleteMapping("/deleteMessage")
    public String deleteMessage(@RequestParam("id") UUID messageId) {
        messageService.deleteMessage(messageId);

        return "message deleted";
    }

    @GetMapping("/findAllMessageByChannelId")
    public ResponseEntity<List<MessageResponse>> findMessageByChannelId(@RequestParam("id") UUID channelId) {

        return ResponseEntity.ok(messageService.findAllMessageByChannelId(channelId));
    }
}

//[ ] 메시지를 보낼 수 있다.
//        [ ] 메시지를 수정할 수 있다.
//        [ ] 메시지를 삭제할 수 있다.
//        [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
