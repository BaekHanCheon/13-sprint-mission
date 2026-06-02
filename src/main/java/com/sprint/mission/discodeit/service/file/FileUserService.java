package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class FileUserService implements UserService {

    FileUserRepository repository = new FileUserRepository();

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
    public void findUserById(UUID id) {
        User user = repository.findUserById(id);
        if (user != null) {
            System.out.println("유저 조회 : ");
            System.out.println(user.toString());
        } else {
            System.out.println("해당 유저가 없습니다.");
        }
    }

    @Override
    public void findAllUser() {
        System.out.println("전체 유저 조회 : ");
        repository.findAllUser();
    }

    @Override
    public void updateUser(UUID id, String property, String value) {
        User user = repository.findUserById(id);
        if (user == null) {
            System.out.println("해당 유저가 없습니다.");
            return;
        }
        switch (property.toUpperCase()) {
            case "PASSWORD": user.updatePassword(value); System.out.println("비밀번호 수정"); break;
            case "EMAIL": user.updateEmail(value); System.out.println("이메일 수정"); break;
            case "USERNAME": user.updateUserName(value); System.out.println("유저이름 수정"); break;
        }
        user.updateUpdatedAt();
        repository.updateUser(user);
    }

    @Override
    public void deleteUser(UUID id) {
        repository.deleteUser(id);
        System.out.println("유저 삭제");
    }
}