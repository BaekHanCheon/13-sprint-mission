package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BinaryContentService {

  private final BinaryContentRepository repository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  public BinaryContentResponse findBinaryContentById(UUID id) {
    BinaryContent binaryContent = repository.findById(id)
        .orElseThrow(() -> new BinaryContentNotFoundException(id));

    return binaryContentMapper.toDto(binaryContent);
  }

  public List<BinaryContentResponse> findAllBinaryContent(List<UUID> idList) {
    return idList.stream()
        .map(id -> repository.findById(id)
            .orElseThrow(() -> new BinaryContentNotFoundException(id)))
        .map(binaryContentMapper::toDto)
        .toList();
  }

  public ResponseEntity<Resource> downloadBinaryContent(UUID id) {
    log.info("파일 다운로드 시작: binaryContentId={}", id);
    BinaryContent binaryContent = repository.findById(id)
        .orElseThrow(() -> new BinaryContentNotFoundException(id));

    ResponseEntity<Resource> response =
        binaryContentStorage.download(binaryContentMapper.toDto(binaryContent));
    log.info("파일 다운로드 완료: binaryContentId={}, size={}, contentType={}",
        id, binaryContent.getSize(), binaryContent.getContentType());
    return response;
  }

}
