package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class User extends Entity implements Serializable {

    private String password;
    private String userName;
    private String email;
    private String phoneNumber;
    private UserType userType;

//    private ArrayList managedChannelList = new ArrayList();
//
//    public ArrayList getManagedChannelList() {
//        return managedChannelList;
//    }
//
//    public void addManagedChannelList(UUID id) {
//        this.managedChannelList.add(id);
//    }

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
    public UUID getId() {
        return super.getId();
    }

    @Override
    public long getUpdatedAt() {
        return super.getUpdatedAt();
    }

    @Override
    public long getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public void updateUpdatedAt() {
        super.updateUpdatedAt();
    }

    public String getPassword() {
        return password;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void updateEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
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
