package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BinaryContentService {

    private final BinaryContentRepository repository;

    public BinaryContentResponse createBinaryContent(BinaryContentCreateRequest request) {
        String savedFileName = repository.saveFile(request.file());
        BinaryContent binaryContent = new BinaryContent(savedFileName, null, null);

        repository.createBinaryContent(binaryContent);
        return BinaryContentResponse.from(binaryContent);
    }

    public BinaryContentResponse findBinaryContentById(UUID id) {
        BinaryContent binaryContent = repository.findBinaryContentById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent not found"));

        byte[] data = repository.readFile(binaryContent.getFileName());
        String contentType = repository.getContentType(binaryContent.getFileName());
        String base64 = Base64.getEncoder().encodeToString(data);

        return BinaryContentResponse.from(binaryContent, contentType, base64);
    }

    public List<BinaryContentResponse> findAllBinaryContent(List<UUID> idList) {
        List<BinaryContent> binaryContents = repository.findAllBinaryContentByIdIn(idList)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent not found"));

        return binaryContents.stream()
                .map(binaryContent -> {
                    byte[] data = repository.readFile(binaryContent.getFileName());
                    String contentType = repository.getContentType(binaryContent.getFileName());
                    String base64 = Base64.getEncoder().encodeToString(data);

                    return BinaryContentResponse.from(binaryContent, contentType, base64);
                })
                .toList();
    }

    public void deleteBinaryContent(UUID id) {
        repository.deleteBinaryContent(id);

    }

}
