package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleBadRequest(IllegalArgumentException e) {
    log.warn("잘못된 요청: {}", e.getMessage());
    return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ProblemDetail handleIllegalState(IllegalStateException e) {
    log.warn("잘못된 상태의 요청: {}", e.getMessage());
    return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ProblemDetail handleNotFound(NoSuchElementException e) {
    log.warn("자원을 찾을 수 없음: {}", e.getMessage());
    return buildProblemDetail(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e) {
    log.error("서버 오류", e);
    return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        "서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
  }

  private ProblemDetail buildProblemDetail(HttpStatus status, String detail) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }
}
