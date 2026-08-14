package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.S3Properties;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final S3Properties s3Properties;

  @Override
  public UUID put(UUID id, byte[] bytes) {
    if (id == null || bytes == null || bytes.length == 0) {
      throw new IllegalArgumentException("File id and bytes must not be empty.");
    }

    s3Client.putObject(PutObjectRequest.builder()
        .bucket(s3Properties.getBucket())
        .key(key(id))
        .build(), RequestBody.fromBytes(bytes));
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    return s3Client.getObject(GetObjectRequest.builder()
        .bucket(s3Properties.getBucket())
        .key(key(id))
        .build(), software.amazon.awssdk.core.sync.ResponseTransformer.toInputStream());
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentResponse binaryContent) {
    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        GetObjectPresignRequest.builder()
            .signatureDuration(s3Properties.getPresignedUrlExpiration())
            .getObjectRequest(GetObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(key(binaryContent.id()))
                .responseContentType(binaryContent.contentType())
                .responseContentDisposition("attachment; filename=\"" + binaryContent.fileName() + "\"")
                .build())
            .build());

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(presignedRequest.url().toString()))
        .build();
  }

  private String key(UUID id) {
    return id.toString();
  }
}
