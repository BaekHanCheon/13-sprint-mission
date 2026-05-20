package com.sprint.mission.discodeit.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class Channel extends Entity {

    private ChannelType type;
    private String name;
    private String description;
    //private boolean isManagerChannel; //매니저만 송신 가능 채널 여부

    //private ArrayList<UUID> managerList = new ArrayList<>(); //채널 매니저 리스트
    private ArrayList<UUID> allowedUserList = new ArrayList<>(); //사용자

    //생성자
    public Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
    }

//    public void addManagerList(UUID manager) { //채널에 매니저 uid 정보 등록
//        this.managerList.add(manager);
//    }
//
//    public void deleteManagerList(UUID manager) { // 채널에 매니저 uid 정보 삭제
//        this.managerList.remove(manager);
//    }
    public void addAllowedUserList(UUID uid) { //채널에 허용 userid 정보 등록
        this.allowedUserList.add(uid);
    }

    public void disallowedList(UUID uid) { // 채널에 허용 userid 정보 삭제
        this.allowedUserList.remove(uid);
    }

    public ArrayList<UUID> getAllowedUserList() {
        return allowedUserList;
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

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public ChannelType getType() {
        return type;
    }

    public void updateType(String type) {
        if(type.equals("PUBLIC")){
            this.type = ChannelType.PUBLIC;
        } else if (type.equals("PRIVATE")) {
            this.type = ChannelType.PRIVATE;
        } else if (type.equals("MANAGER")) {
            this.type = ChannelType.MANAGER;
        } else {
            System.out.println("올바른 채널 타입이 아닙니다.");
        }

    }

//    public boolean isManagerChannel() {
//        return isManagerChannel;
//    }
//
//    public void setManagerChannel() {
//        isManagerChannel = true;
//    }
//
//    public void disableManagerChannel(){
//        isManagerChannel = false;
//    }
//
//    public ArrayList<UUID> getManagerList() {
//        return managerList;
//    }

    @Override
    public String toString() {
        return "Channel{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", allowedList=" + allowedUserList +
                "} " + super.toString();
    }
}