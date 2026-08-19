package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.S3Config;
import com.sprint.mission.discodeit.config.S3Properties;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Assumptions;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * 실제 AWS S3 통합 테스트입니다.
 *
 * <p>프로젝트 루트의 {@code .env}에 {@code AWS_S3_BUCKET}과 {@code AWS_S3_REGION}을
 * 정의해야 합니다. AWS 자격 증명은 {@link S3Config}가 AWS CLI의 {@code discodeit} 프로파일에서 찾습니다.</p>
 */
@Tag("aws")
@TestInstance(Lifecycle.PER_CLASS)
class AWSS3Test {

  private static final String CONTENT_TYPE = "text/plain";

  private S3Properties s3Properties;
  private S3Client s3Client;
  private S3Presigner s3Presigner;
  private String key;

  @BeforeEach
  void setUp() throws IOException {
    Properties environment = loadDotEnv();
    String bucket = environment.getProperty("AWS_S3_BUCKET");

    Assumptions.assumeTrue(bucket != null && !bucket.isBlank(),
        ".env에 AWS_S3_BUCKET을 설정해야 AWS S3 통합 테스트를 실행할 수 있습니다.");

    s3Properties = new S3Properties();
    s3Properties.setBucket(bucket);
    s3Properties.setRegion(environment.getProperty("AWS_S3_REGION", "ap-northeast-2"));
    s3Properties.setPresignedUrlExpiration(Duration.ofSeconds(Long.parseLong(
        environment.getProperty("AWS_S3_PRESIGNED_URL_EXPIRATION", "600"))));

    S3Config s3Config = new S3Config();
    s3Client = s3Config.s3Client(s3Properties);
    s3Presigner = s3Config.s3Presigner(s3Properties);
    key = "tests/aws-s3/" + UUID.randomUUID() + ".txt";
  }

  @AfterEach
  void tearDown() {
    if (s3Client != null && key != null) {
      s3Client.deleteObject(request -> request.bucket(s3Properties.getBucket()).key(key));
      s3Client.close();
      s3Presigner.close();
    }
  }

  @Test
  void upload() {
    byte[] content = "AWS S3 업로드 테스트".getBytes(StandardCharsets.UTF_8);

    s3Client.putObject(PutObjectRequest.builder()
        .bucket(s3Properties.getBucket())
        .key(key)
        .contentType(CONTENT_TYPE)
        .build(), RequestBody.fromBytes(content));

    assertThat(s3Client.headObject(request -> request.bucket(s3Properties.getBucket()).key(key))
        .contentLength()).isEqualTo((long) content.length);
  }

  @Test
  void download() {
    byte[] content = "AWS S3 download test".getBytes(StandardCharsets.UTF_8);
    upload(content);

    ResponseBytes<GetObjectResponse> downloaded = s3Client.getObject(
        GetObjectRequest.builder().bucket(s3Properties.getBucket()).key(key).build(),
        ResponseTransformer.toBytes());

    assertThat(downloaded.asByteArray()).isEqualTo(content);
    assertThat(downloaded.response().contentType()).isEqualTo(CONTENT_TYPE);
  }

  @Test
  void generatePresignedUrl() {
    upload("AWS S3 presigned URL test".getBytes(StandardCharsets.UTF_8));

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        GetObjectPresignRequest.builder()
            .signatureDuration(s3Properties.getPresignedUrlExpiration())
            .getObjectRequest(request -> request.bucket(s3Properties.getBucket()).key(key))
            .build());

    assertThat(presignedRequest.url().toString())
        .contains(key)
        .contains("X-Amz-");
  }

  private void upload(byte[] content) {
    s3Client.putObject(PutObjectRequest.builder()
        .bucket(s3Properties.getBucket())
        .key(key)
        .contentType(CONTENT_TYPE)
        .build(), RequestBody.fromBytes(content));
  }

  private Properties loadDotEnv() throws IOException {
    Properties properties = new Properties();
    Path dotEnv = Path.of(".env");

    Assumptions.assumeTrue(Files.exists(dotEnv),
        "프로젝트 루트에 .env 파일이 있어야 AWS S3 통합 테스트를 실행할 수 있습니다.");

    try (InputStream inputStream = Files.newInputStream(dotEnv)) {
      properties.load(inputStream);
    }
    return properties;
  }

}
