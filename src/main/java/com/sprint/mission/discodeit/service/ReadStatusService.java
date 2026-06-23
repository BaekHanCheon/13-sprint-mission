package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final ReadStatusRepository repository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusResponse createReadStatus(ReadStatusCreateRequest request){
        ReadStatus readStatus = request.toEntity();
        userRepository.findUserById(readStatus.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id: " + readStatus.getUserId()));
        channelRepository.findChannelById(readStatus.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다. id: " + readStatus.getChannelId()));

        repository.createReadStatus(readStatus);
        return ReadStatusResponse.from(readStatus);
    }

    public ReadStatusResponse findReadStatusById(UUID readStatusId){
        ReadStatus readStatus = getReadStatusOrThrow(readStatusId);

        return ReadStatusResponse.from(readStatus);
    }


    public List<ReadStatusResponse> findAllReadStatusByUserId(UUID userId){
        log.info("userId : {}의 readstatus 전체 조회", userId);
        return repository.findAllReadStatusByUserId(userId).stream().map(ReadStatusResponse::from).toList();
    }

    public ReadStatusResponse updateReadStatus(ReadStatusUpdateRequest request){
        ReadStatus readStatus = getReadStatusOrThrow(request.id());
        readStatus.updateLastReadAt(request.lastReadAt());
        readStatus.updateUpdatedAt();

        repository.updateReadStatus(readStatus);

        return ReadStatusResponse.from(readStatus);
    }

    public void deleteReadStatus(UUID readStatusId){
        repository.deleteReadStatus(readStatusId);
        System.out.println("readstatus 삭제됨");}

    private ReadStatus getReadStatusOrThrow(UUID id){
        return repository.findReadStatusById(id).orElseThrow(() -> new NoSuchElementException("해당 ReadStatus가 없습니다."));
    }
}
