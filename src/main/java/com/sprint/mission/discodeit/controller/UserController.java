package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


  private final UserService userService;
  private final UserStatusService userStatusService;


  @Operation(summary = "사용자 등록", description = "프로필 이미지와 함께 신규 사용자를 등록합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request, profile));
  }

  @Operation(summary = "사용자 정보 수정", description = "사용자 정보와 프로필 이미지를 수정합니다.")
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> updateUser(
      @Parameter(description = "수정할 사용자 ID") @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, request, profile));
  }

  @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "삭제할 사용자 ID") @PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "전체 사용자 조회", description = "모든 사용자 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<UserResponse>> findAllUser() {

    return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUser());
  }

  @Operation(summary = "사용자 온라인 상태 수정", description = "사용자의 온라인 상태(접속 시각)를 업데이트합니다.")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> updateUserStatusByUserId(
      @Parameter(description = "대상 사용자 ID") @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request) {

    return ResponseEntity.status(HttpStatus.OK).body(userStatusService.updateUserStatusByUserId(userId, request));
  }

}
//[ ] 사용자를 등록할 수 있다.
//        [ ] 사용자 정보를 수정할 수 있다.
//[ ] 사용자를 삭제할 수 있다.
//        [ ] 모든 사용자를 조회할 수 있다.
//[ ] 사용자의 온라인 상태를 업데이트할 수 있다.
