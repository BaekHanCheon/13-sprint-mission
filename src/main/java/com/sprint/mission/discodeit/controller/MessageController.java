package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "메시지 관리 API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @Operation(summary = "메시지 전송", description = "첨부파일과 함께 메시지를 전송합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> createMessage(
            @RequestPart("messageCreateRequest") MessageCreateRequest request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(request, attachments));
    }

    @Operation(summary = "메시지 수정", description = "메시지 내용을 수정합니다.")
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponse> updateMessage(
            @Parameter(description = "수정할 메시지 ID") @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(messageService.updateMessage(messageId, request));
    }

    @Operation(summary = "메시지 삭제", description = "메시지를 삭제합니다.")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(
            @Parameter(description = "삭제할 메시지 ID") @PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);

        return ResponseEntity.status(HttpStatus.OK).body("message deleted");
    }

    @Operation(summary = "채널 메시지 목록 조회", description = "특정 채널의 메시지 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<MessageResponse>> findMessageByChannelId(
            @Parameter(description = "채널 ID") @RequestParam("channelId") UUID channelId) {

        return ResponseEntity.status(HttpStatus.OK).body(messageService.findAllMessageByChannelId(channelId));
    }
}

//[ ] 메시지를 보낼 수 있다.
//        [ ] 메시지를 수정할 수 있다.
//        [ ] 메시지를 삭제할 수 있다.
//        [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
