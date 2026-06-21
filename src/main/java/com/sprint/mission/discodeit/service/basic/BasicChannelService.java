package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class BasicChannelService implements ChannelService {
    private final ChannelRepository repository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public void addAllowedUserList(UUID channelId, UUID userId) {
        Channel channel = getChannelOrThrow(channelId);
        channel.addAllowedUserList(userId);
        repository.updateChannel(channel);
    }

    public void disallowChannelList(UUID channelId, UUID userId) {
        Channel channel = getChannelOrThrow(channelId);

        channel.disallowedList(userId);
        repository.updateChannel(channel);
    }

    @Override
    public ChannelResponse createPublicChannel(ChannelPublicCreateRequest request) {
        boolean isDuplicate = repository.findAllChannel().stream()
                .anyMatch(c -> c.getName().equals(request.title()));
        if (isDuplicate) {
            throw new IllegalStateException("이미 존재하는 채널 이름입니다: " + request.title());
        }
        Channel channel = request.toEntity();

        repository.createChannel(channel);

        log.info("{} 채널 생성됨", channel.getName());

        return ChannelResponse.from(channel, null);
    }

    @Override
    public ChannelResponse createPrivateChannel(ChannelPrivateCreateRequest request) {

        Channel channel = request.toEntity();

        List<UUID> userIdList = request.userIdList();
        userIdList.forEach(id -> channel.addAllowedUserList(id));
        // 각 유저마다 ReadStatus 생성
        userIdList.forEach(userId -> {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId());
            readStatusRepository.createReadStatus(readStatus);
        });

        repository.createChannel(channel);
        log.info("private 채널 생성됨");
        return ChannelResponse.from(channel, null);
    }

    @Override
    public ChannelResponse findChannelById(UUID channelId) {
        Channel channel = getChannelOrThrow(channelId);
        Instant lastMessageAt = getLastMessageAt(channelId);
        return ChannelResponse.from(channel, lastMessageAt);
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        return repository.findAllChannel().stream()
                .filter(channel ->
                        channel.getType() == ChannelType.PUBLIC
                                || channel.getAllowedUserList().contains(userId)
                )
                .map(channel -> ChannelResponse.from(channel, getLastMessageAt(channel.getId())))
                .toList();
    }

    @Override
    public ChannelResponse updateChannel(ChannelUpdateRequest request) {
        Channel channel = getChannelOrThrow(request.id());

        // PRIVATE 채널 수정 불가
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.updateName(request.name());
        channel.updateDescription(request.description());
        channel.updateUpdatedAt();
        repository.updateChannel(channel);
        Instant lastMessageAt = getLastMessageAt(request.id());

        return ChannelResponse.from(channel, lastMessageAt);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        // 연관 Message 삭제
        messageRepository.findAllMessageByChannelId(channelId)
                .forEach(message -> messageRepository.deleteMessage(message.getId()));

        // 연관 ReadStatus 삭제
        readStatusRepository.findAllByChannelId(channelId)
                .forEach(rs -> readStatusRepository.deleteReadStatus(rs.getId()));

        repository.deleteChannel(channelId);
    }

    private Channel getChannelOrThrow(UUID channelId) {
        return repository.findChannelById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
    }

    private Instant getLastMessageAt(UUID channelId) {
        List<Message> messageList = messageRepository.findAllMessageByChannelId(channelId);
        if (messageList.isEmpty()) {
            return null;
        }
        return messageList.stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(null);
    }
}