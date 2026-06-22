package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final UserStatusService userStatusService;


    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {

        return ResponseEntity.ok(userService.createUser(request));
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(userService.updateUser(request));
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findAll")
    public List<UserResponse> findAllUser() {

        return userService.findAllUser();
    }

    @PatchMapping("/updateUserStatusByUserId")
    public ResponseEntity<UserStatusResponse> updateUserStatusByUserId (@RequestBody UserStatusUpdateRequest request) {

        return ResponseEntity.ok(userStatusService.updateUserStatusByUserId(request));
    }

}
//[ ] 사용자를 등록할 수 있다.
//        [ ] 사용자 정보를 수정할 수 있다.
//[ ] 사용자를 삭제할 수 있다.
//        [ ] 모든 사용자를 조회할 수 있다.
//[ ] 사용자의 온라인 상태를 업데이트할 수 있다.