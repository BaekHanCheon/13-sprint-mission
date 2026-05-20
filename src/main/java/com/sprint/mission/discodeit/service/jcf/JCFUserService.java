package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final Map<UUID,User> userData = new HashMap<>();;

    private boolean isExistEmail(User user){
        return userData.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()));
    }
    private boolean isExistPhoneNumber(User user){
        return userData.values().stream().anyMatch(u -> u.getEmail().equals(user.getPhoneNumber()));
    }

    @Override
    public void createUser(User user) {//생성
        if(isExistEmail(user)) {
            System.out.println("이메일 중복 생성불가");
        }
        else if(isExistPhoneNumber(user)){
            System.out.println("폰넘버 중복 생성불가");
        }else{
            userData.put(user.getId(),user);
            System.out.println("유저 생성");
        }
    }

    @Override
    public void readUser(UUID id) { //조회
        System.out.println("유저 조회 : ");
        System.out.println(userData.get(id).toString());
    }

    @Override
    public void readAllUser() { //전체조회
        System.out.println("전체 유저 조회 : ");
        userData.values().stream().toList().forEach(System.out::println);
    }

    @Override
    public void modifyUser(UUID id, String property, String value) { //수정
        User user = userData.get(id);
        property = property.toUpperCase();
        switch (property){
            case "PASSWORD" : user.updatePassword(value); System.out.println("비밀번호 수정");break;
            case "EMAIL" : user.updateEmail(value); System.out.println("이메일 수정");break;
            case "USERNAME" : user.setUserName(value); System.out.println("유저이름 수정");break;
        }
        user.updateUpdatedAt();
    }

    @Override
    public void deleteUser(UUID id) { //삭제
        userData.remove(id);
        System.out.println("유저 삭제");
    }
}
