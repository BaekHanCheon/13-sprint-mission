package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class Channel extends Entity implements Serializable {

    private ChannelType type;
    private String name;
    private String description;

    private ArrayList<UUID> allowedUserList = new ArrayList<>(); //사용자

    //생성자
    public Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
    }

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
        if("PUBLIC".equals(type)){
            this.type = ChannelType.PUBLIC;
        } else if ("PRIVATE".equals(type)) {
            this.type = ChannelType.PRIVATE;
        } else if ("MANAGER".equals(type)) {
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