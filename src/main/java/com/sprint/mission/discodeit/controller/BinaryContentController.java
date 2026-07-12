package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;

  @Override
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResponse> findBinaryContentById(
      @PathVariable("binaryContentId") UUID id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findBinaryContentById(id));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<BinaryContentResponse>> findAllBinaryContentById(
      @RequestParam("binaryContentIds") List<UUID> idList) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findAllBinaryContent(idList));
  }

  @Override
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> downloadBinaryContentById(
      @PathVariable("binaryContentId") UUID id) {
    return binaryContentService.downloadBinaryContent(id);
  }
}
