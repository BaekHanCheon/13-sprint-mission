package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository repository;

    // 생성자로 Repository를 받아와서 저장(의존성 주입)
    public BasicUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUser(User user) {
        if (repository.isExistEmail(user)) {
            System.out.println("이메일 중복 생성불가");
        } else if (repository.isExistPhoneNumber(user)) {
            System.out.println("폰넘버 중복 생성불가");
        } else {
            repository.createUser(user);
            System.out.println("유저 생성");
        }
    }

    @Override
    public void readUser(UUID id) {
        User user = repository.readUser(id);
        if (user != null) {
            System.out.println("유저 조회 : ");
            System.out.println(user.toString());
        } else {
            System.out.println("해당 유저가 없습니다.");
        }
    }

    @Override
    public void readAllUser() {
        System.out.println("전체 유저 조회 : ");
        repository.readAllUser();
    }

    @Override
    public void modifyUser(UUID id, String property, String value) {
        User user = repository.readUser(id);
        if (user == null) {
            System.out.println("해당 유저가 없습니다.");
            return;
        }
        switch (property.toUpperCase()) {
            case "PASSWORD": user.updatePassword(value); System.out.println("비밀번호 수정"); break;
            case "EMAIL": user.updateEmail(value); System.out.println("이메일 수정"); break;
            case "USERNAME": user.setUserName(value); System.out.println("유저이름 수정"); break;
        }
        user.updateUpdatedAt();
        repository.modifyUser(user);
    }

    @Override
    public void deleteUser(UUID id) {
        repository.deleteUser(id);
        System.out.println("유저 삭제");
    }
}