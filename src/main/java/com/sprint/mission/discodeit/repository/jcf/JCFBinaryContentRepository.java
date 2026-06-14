package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
@Slf4j
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContentData = new HashMap();

    private final Path uploadPath;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            // 이미지
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg",
            // 문서
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            // 텍스트
            ".txt", ".md", ".csv", ".json"
    );

    public JCFBinaryContentRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);
            log.info("업로드 디렉토리 준비 완료: {}", uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: " + uploadPath, e);
        }
    }

    @Override
    public void createBinaryContent(BinaryContent binaryContent) {

        binaryContentData.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public Optional<BinaryContent> findBinaryContentById(UUID id) {
        BinaryContent binaryContent = binaryContentData.get(id);
        return Optional.ofNullable(binaryContent);
    }

    @Override
    public List<BinaryContent> findAllBinaryContentByIdIn(List<UUID> idList) {
        //return load().values().stream().sorted(Comparator.comparing(BinaryContent::getCreatedAt)).toList();
        return null;
    }

    @Override
    public void deleteBinaryContent(UUID id) {

        Path path = uploadPath.resolve(binaryContentData.get(id).getFileName()).normalize();
        try {
            Files.deleteIfExists(path);
            log.info("파일 삭제 완료: {}", path);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + path, e);
        }
        binaryContentData.remove(id);


    }




    public String saveFile(Path sourcePath) {
        String originFileName = sourcePath.getFileName().toString();

        int dotIndex = originFileName.lastIndexOf(".");
        String extension = (dotIndex >= 0) ? originFileName.substring(dotIndex).toLowerCase() : "";
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + extension);
        }

        String savedFileName = UUID.randomUUID().toString().replace("-", "") + extension;
        Path targetPath = uploadPath.resolve(savedFileName).normalize();

        try {
            Files.copy(sourcePath, targetPath);
            log.info("파일 저장 완료: {} (원본: {}, 크기: {} bytes)", savedFileName, originFileName, Files.size(sourcePath));
            return savedFileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + originFileName, e);
        }
    }

    public ResponseEntity<Resource> getImage(String fileName) {
        Path requested = uploadPath.resolve(fileName).normalize();

        if (!Files.exists(requested) || Files.isDirectory(requested)) {
            return ResponseEntity.notFound().build();
        }
        try {
            Resource resource = new UrlResource(requested.toUri());
            String contentType = Files.probeContentType(requested);
            MediaType mediaType = (contentType == null)
                    ? MediaType.APPLICATION_OCTET_STREAM
                    : MediaType.parseMediaType(contentType);
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } catch (IOException e) {
            log.error("파일 응답 실패: fileName={}", fileName, e);
            return ResponseEntity.internalServerError().build();
        }

    }

}
