package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/find")
    public ResponseEntity<BinaryContentResponse> findBinaryContentById(@RequestParam("binaryContentId") UUID id) {

        return ResponseEntity.ok(binaryContentService.findBinaryContentById(id));
    }

    @GetMapping("/findAllBinaryContentById")
    public ResponseEntity<List<BinaryContentResponse>> findAllBinaryContentById(@RequestParam("id") List<UUID> idList) {

        return ResponseEntity.ok(binaryContentService.findAllBinaryContent(idList));
    }


}

//[ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.