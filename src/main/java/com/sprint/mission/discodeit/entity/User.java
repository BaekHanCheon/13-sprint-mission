package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public class User extends Entity implements Serializable {

    private String password;
    private String userName;
    private String email;
    private String phoneNumber;
    private UserType userType;

    //생성자
    public User(String password, String userName, String email, String phoneNumber,UserType userType){
        super();
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    //getter setter


    @Override
    public void updateUpdatedAt() {
        super.updateUpdatedAt();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "유저 : " +
                "password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                super.toString();
    }

}
