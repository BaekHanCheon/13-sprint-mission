package com.sprint.mission.discodeit.entity;

public enum UserType {
    GENERAL("일반"),MANAGER("매니저");

    private final String tag;

    UserType(String tag) {
        this.tag = tag;
    }
}
