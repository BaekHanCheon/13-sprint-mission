package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "Channel", description = "채널 관리 API")
public interface ChannelApi {

  @Operation(summary = "공개 채널 생성", description = "공개 채널을 생성합니다.")
  ResponseEntity<ChannelResponse> createPublicChannel(ChannelPublicCreateRequest request);

  @Operation(summary = "비공개 채널 생성", description = "참여자를 지정하여 비공개 채널을 생성합니다.")
  ResponseEntity<ChannelResponse> createPrivateChannel(ChannelPrivateCreateRequest request);

  @Operation(summary = "채널 정보 수정", description = "공개 채널의 정보를 수정합니다.")
  ResponseEntity<ChannelResponse> updateChannel(
      @Parameter(description = "수정할 채널 ID") UUID channelId,
      ChannelUpdateRequest request
  );

  @Operation(summary = "채널 삭제", description = "채널을 삭제합니다.")
  ResponseEntity<Void> deleteChannel(
      @Parameter(description = "삭제할 채널 ID") UUID channelId
  );

  @Operation(summary = "사용자 채널 목록 조회", description = "특정 사용자가 볼 수 있는 모든 채널 목록을 조회합니다.")
  ResponseEntity<List<ChannelResponse>> findChannelByUserId(
      @Parameter(description = "사용자 ID") UUID userId
  );
}
