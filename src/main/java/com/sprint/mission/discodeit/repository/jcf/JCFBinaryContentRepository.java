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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

  public JCFBinaryContentRepository(
      @Value("${discodeit.repository.file-directory:.discodeit}") String uploadDir) {
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
  public Optional<List<BinaryContent>> findAllBinaryContentByIdIn(List<UUID> idList) {
    return Optional.of(idList.stream()
        .map(binaryContentData::get)
        .filter(Objects::nonNull)
        .toList());
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


  @Override
  public String saveFile(MultipartFile file) {
    String originFileName = StringUtils.hasText(file.getOriginalFilename())
        ? file.getOriginalFilename() : "upload";

    int dotIndex = originFileName.lastIndexOf(".");
    String extension = (dotIndex >= 0) ? originFileName.substring(dotIndex).toLowerCase() : "";
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + extension);
    }

    String savedFileName = UUID.randomUUID().toString().replace("-", "") + extension;
    Path targetPath = uploadPath.resolve(savedFileName).normalize();

    try {
      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
      log.info("파일 저장 완료: {} (원본: {}, 크기: {} bytes)", savedFileName, originFileName,
          file.getSize());
      return savedFileName;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패: " + originFileName, e);
    }
  }

  @Override
  public byte[] readFile(String fileName) {
    Path requested = uploadPath.resolve(fileName).normalize();
    if (!Files.exists(requested) || Files.isDirectory(requested)) {
      throw new NoSuchElementException("파일을 찾을 수 없습니다: " + fileName);
    }
    try {
      return Files.readAllBytes(requested);
    } catch (IOException e) {
      throw new RuntimeException("파일 읽기 실패: " + fileName, e);
    }
  }

  @Override
  public String getContentType(String fileName) {
    Path requested = uploadPath.resolve(fileName).normalize();
    try {
      String contentType = Files.probeContentType(requested);
      return (contentType != null) ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
    } catch (IOException e) {
      return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
  }

}
