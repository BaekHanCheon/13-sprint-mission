package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UserStatus;
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
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final static Path BINARY_PATH = Path.of("data/binaryContents.ser");

  private final Path uploadPath;

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
      // 이미지
      ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg",
      // 문서
      ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
      // 텍스트
      ".txt", ".md", ".csv", ".json"
  );

  public FileBinaryContentRepository(
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
    Map<UUID, BinaryContent> data = load();
    data.put(binaryContent.getId(), binaryContent);
    save(data);
  }

  @Override
  public Optional<BinaryContent> findBinaryContentById(UUID id) {
    BinaryContent binaryContent = load().get(id);
    return Optional.ofNullable(binaryContent);
  }

  @Override
  public Optional<List<BinaryContent>> findAllBinaryContentByIdIn(List<UUID> idList) {
    Map<UUID, BinaryContent> data = load();
    return Optional.of(idList.stream()
        .map(data::get)
        .filter(Objects::nonNull)
        .toList());
  }

  @Override
  public void deleteBinaryContent(UUID id) {
    Map<UUID, BinaryContent> data = load();
    BinaryContent binaryContent = data.get(id);
    if (binaryContent == null) {
      throw new NoSuchElementException("삭제할 첨부파일이 없습니다: " + id);
    }

    Path path = uploadPath.resolve(binaryContent.getFileName()).normalize();

    try {
      Files.deleteIfExists(path);
      log.info("파일 삭제 완료: {}", path);
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 실패: " + path, e);
    }
    data.remove(id);
    save(data);

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

  private void save(Map<UUID, BinaryContent> storage) {
    Path parent = BINARY_PATH.getParent();
    if (parent != null) {
      try {
        Files.createDirectories(parent);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new BufferedOutputStream(Files.newOutputStream(BINARY_PATH)))) {
      oos.writeObject(new HashMap<>(storage));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<UUID, BinaryContent> load() {
      if (!Files.exists(BINARY_PATH)) {
          return new HashMap<>();
      }
    try (ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(Files.newInputStream(BINARY_PATH)))) {
      return (Map<UUID, BinaryContent>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }


}
