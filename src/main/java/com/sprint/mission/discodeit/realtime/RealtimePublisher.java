package com.sprint.mission.discodeit.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RealtimePublisher {
  private final ApplicationEventPublisher events;
  private final ObjectMapper mapper;

  public void websocket(String destination, Object data) {
    events.publishEvent(new RealtimeEvent(UUID.randomUUID(), Instant.now(), destination,
        null, null, mapper.valueToTree(data)));
  }
}
