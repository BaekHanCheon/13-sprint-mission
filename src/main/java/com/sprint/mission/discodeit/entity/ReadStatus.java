package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends Entity implements Serializable {
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    @Builder
    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    public void updateLastReadAt(Instant lastReadAt) {

        this.lastReadAt = lastReadAt;
    }
}
