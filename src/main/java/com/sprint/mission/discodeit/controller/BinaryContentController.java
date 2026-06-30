package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "바이너리 컨텐츠(첨부파일) 조회 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @Operation(summary = "바이너리 컨텐츠 단건 조회", description = "ID로 바이너리 파일 1개를 조회합니다.")
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResponse> findBinaryContentById(
            @Parameter(description = "바이너리 컨텐츠 ID") @PathVariable("binaryContentId") UUID id) {

        return ResponseEntity.status(HttpStatus.OK).body(binaryContentService.findBinaryContentById(id));
    }

    @Operation(summary = "바이너리 컨텐츠 다건 조회", description = "여러 ID로 바이너리 파일 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<BinaryContentResponse>> findAllBinaryContentById(
            @Parameter(description = "바이너리 컨텐츠 ID 목록") @RequestParam("binaryContentIds") List<UUID> idList) {

        return ResponseEntity.status(HttpStatus.OK).body(binaryContentService.findAllBinaryContent(idList));
    }


}

//[ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.