package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e) {
        log.warn("잘못된 요청: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException e) {
        log.warn("자원을 찾을 수 없음: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("서버 오류", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }

    public record ErrorResponse(int status, String message) {}
}

//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleBadRequest(IllegalArgumentException e, Model model){
//        log.warn("잘못 된 요청 {}", e.getMessage());
//        model.addAttribute("message", e.getMessage());
//        return"error/400";
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 에러 발생
//    public String handleNotFound(NoSuchElementException e, Model model) {
//        log.warn("요청한 자원을 찾을 수 없습니다: {}", e.getMessage());
//        model.addAttribute("message", e.getMessage());
//        return "error/404";
//    }
//
//    //그밖에 예상 못한 예외
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleException(Exception e, Model model){
//        log.error("서버 오류", e);
//        model.addAttribute("message", "서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요");
//        return"error/500";
//    }
//
//}
