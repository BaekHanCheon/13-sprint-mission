package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentStorageException extends BinaryContentException {

  public BinaryContentStorageException(String fileName, Throwable cause) {
    super(ErrorCode.BINARY_CONTENT_STORAGE_FAILED,
        Map.of("fileName", String.valueOf(fileName)), cause);
  }
}
