package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public class Channel extends Entity implements Serializable {

    private ChannelType type;
    private String name;
    private String description;

    private ArrayList<UUID> allowedUserList = new ArrayList<>(); //사용자

    //생성자
    @Builder(builderMethodName = "publicChannelBuilder")
    public Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
    }

    @Builder(builderMethodName = "privateChannelBuilder")
    public Channel(ArrayList<UUID> allowedUserList) {
        super();
        this.allowedUserList = allowedUserList;
    }

    public void addAllowedUserList(UUID uid) { //채널에 허용 userid 정보 등록
        this.allowedUserList.add(uid);
    }

    public void disallowedList(UUID uid) { // 채널에 허용 userid 정보 삭제
        this.allowedUserList.remove(uid);
    }

    //getter setter

    @Override
    public void updateUpdatedAt() {
        super.updateUpdatedAt();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateType(ChannelType type) {
        if(type == ChannelType.PUBLIC){
            this.type = ChannelType.PUBLIC;
        } else if (type == ChannelType.PRIVATE){
            this.type = ChannelType.PRIVATE;
        } else if (type == ChannelType.MANAGER){
            this.type = ChannelType.MANAGER;
        } else {
            System.out.println("올바른 채널 타입이 아닙니다.");
        }

    }

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