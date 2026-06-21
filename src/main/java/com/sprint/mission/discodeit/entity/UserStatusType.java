package com.sprint.mission.discodeit.entity;

public enum UserStatusType {
    ONLINE("온라인"),OFFLINE("오프라인"),AWAY("자리비움");

    private final String tag;

    UserStatusType(String tag) {
        this.tag = tag;
    }
}
