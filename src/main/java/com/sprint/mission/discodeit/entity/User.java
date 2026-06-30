package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class User extends Entity implements Serializable {

  private String password;
  private String userName;
  private String email;
  private String phoneNumber;
  private UserType userType;
  private UUID profileId;
  private UUID userStatusId;
  private boolean isOnline;

  //생성자
  @Builder
  public User(String password, String userName, String email, String phoneNumber, UserType userType,
      UUID profileId) {
    super();
    this.password = password;
    this.userName = userName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.userType = userType;
    this.profileId = profileId;
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

  public void updateUserStatusId(UUID userStatusId) {
    this.userStatusId = userStatusId;
  }

  public void updatePhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void updateUserType(UserType userType) {
    this.userType = userType;
  }

  public void updateProfileId(UUID profileId) {
    this.profileId = profileId;
  }

  public void updateOnline(boolean online) {
    isOnline = online;
  }


}
