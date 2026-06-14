package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter @AllArgsConstructor
public class UserStatus extends Entity{
    private final UUID userId;
    private Instant lastOnline;

    @Builder
    public UserStatus(UUID userId){
        this.userId = userId;
        this.lastOnline = Instant.now();
    }

    public boolean isOnline(){
        return Instant.now().minusSeconds(300).isBefore(lastOnline);
    }

    public void updateLastOnline(){
        this.lastOnline = Instant.now();
    }

}
