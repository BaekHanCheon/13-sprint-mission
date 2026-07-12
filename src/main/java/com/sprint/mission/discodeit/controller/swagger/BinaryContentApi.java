package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "바이너리 콘텐츠 API")
public interface BinaryContentApi {

  @Operation(summary = "바이너리 콘텐츠 단건 조회", description = "ID로 바이너리 파일 1개를 조회합니다.")
  ResponseEntity<BinaryContentResponse> findBinaryContentById(
      @Parameter(description = "바이너리 콘텐츠 ID") UUID id
  );

  @Operation(summary = "바이너리 콘텐츠 다건 조회", description = "여러 ID로 바이너리 파일 목록을 조회합니다.")
  ResponseEntity<List<BinaryContentResponse>> findAllBinaryContentById(
      @Parameter(description = "바이너리 콘텐츠 ID 목록") List<UUID> idList
  );

  @Operation(summary = "바이너리 콘텐츠 다운로드", description = "ID로 바이너리 파일 1개를 다운로드합니다.")
  ResponseEntity<Resource> downloadBinaryContentById(
      @Parameter(description = "바이너리 콘텐츠 ID") UUID id
  );
}
