package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class DiscodeitException extends RuntimeException {

  final Instant timeStamp;
  final ErrorCode errorCode;
  final Map<String, Object> details; // 예외 발생 상황에 대한 추가정보를 저장하기 위한 속성

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    this(errorCode, details, null);
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.timeStamp = Instant.now();
    this.errorCode = errorCode;
    this.details = Map.copyOf(details);
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public Instant getTimeStamp() {
    return timeStamp;
  }

  public Map<String, Object> getDetails() {
    return details;
  }
}
