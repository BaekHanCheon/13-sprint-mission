package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter @AllArgsConstructor
public class UserStatus extends Entity implements Serializable {
    private final UUID userId;
    private Instant lastOnline;
    private UserStatusType userStatus;

    @Builder
    public UserStatus(UUID userId){
        this.userId = userId;
        this.lastOnline = Instant.now();
        this.userStatus = UserStatusType.ONLINE;
    }

    public boolean isOnline(){
        return Instant.now().minusSeconds(300).isBefore(lastOnline);
    }

    public void updateLastOnline(Instant lastOnline){
        this.lastOnline = lastOnline;
    }

    public void updateUserStatus(UserStatusType userStatus){
        this.userStatus = userStatus;
    }

}
