package com.sprint.mission.discodeit.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@ConfigurationProperties("discodeit.storage.s3")
public class S3Properties {

  /**
   * 버킷 이름.
   */
  private String bucket;

  /**
   * 리전(예: ap-northeast-2 서울).
   */
  private String region = "ap-northeast-2";

  /** AWS CLI shared credentials profile name. */
  private String profile = "discodeit";

  /**
   * 커스텀 엔드포인트. 비어 있으면 실제 AWS, 값이 있으면 로컬 목(S3Mock/LocalStack).
   */
  private String endpoint;

  /**
   * presigned URL 유효 시간(분). 짧을수록 안전.
   */
  @DurationUnit(ChronoUnit.SECONDS)
  private Duration presignedUrlExpiration = Duration.ofMinutes(10);

}
