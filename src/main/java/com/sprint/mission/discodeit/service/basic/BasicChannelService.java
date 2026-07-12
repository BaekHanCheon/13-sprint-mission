package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository repository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  public void addAllowedUserList(UUID channelId, UUID userId) {
    Channel channel = getChannelOrThrow(channelId);
    channel.addAllowedUserList(userId);
    repository.save(channel);
  }

  public void disallowChannelList(UUID channelId, UUID userId) {
    Channel channel = getChannelOrThrow(channelId);

    channel.disallowedList(userId);
    repository.save(channel);
  }

  @Override
  @Transactional
  public ChannelResponse createPublicChannel(ChannelPublicCreateRequest request) {
    // PRIVATE 채널은 name이 null이므로 null-안전 비교가 필요하다.
    boolean isDuplicate = repository.findAll().stream()
        .anyMatch(c -> Objects.equals(c.getName(), request.name()));
    if (isDuplicate) {
      throw new IllegalStateException("이미 존재하는 채널 이름입니다: " + request.name());
    }
    Channel channel = request.toEntity();

    repository.save(channel);

    log.info("{} 채널 생성됨", channel.getName());

    return channelMapper.toDto(channel, List.of(), null);
  }

  @Override
  @Transactional
  public ChannelResponse createPrivateChannel(ChannelPrivateCreateRequest request) {

    Channel channel = request.toEntity();

    List<UUID> userIdList = request.participantIds();
    // toEntity()에서 이미 allowedUserList를 채우므로 중복 추가하지 않는다.
    // 각 유저마다 ReadStatus 생성

    userIdList.forEach(userId -> {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new NoSuchElementException("해당 id 유저가 없습니다."));
      ReadStatus readStatus = new ReadStatus(user, channel, null);
      readStatusRepository.save(readStatus);
    });

    repository.save(channel);
    log.info("private 채널 생성됨");
    return toResponse(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelResponse findChannelById(UUID channelId) {
    Channel channel = getChannelOrThrow(channelId);
    return toResponse(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResponse> findAllByUserId(UUID userId) {
    List<Channel> channels = repository.findAll().stream()
        .filter(channel ->
            channel.getType() == ChannelType.PUBLIC
                || channel.getAllowedUserList().contains(userId)
        )
        .toList();

    Map<UUID, User> userById = loadParticipants(channels);
    return channels.stream()
        .map(channel -> channelMapper.toDto(
            channel,
            participantsOf(channel, userById),
            getLastMessageAt(channel)))
        .toList();
  }

  @Override
  @Transactional
  public ChannelResponse updateChannel(UUID channelId, ChannelUpdateRequest request) {
    Channel channel = getChannelOrThrow(channelId);

    // PRIVATE 채널 수정 불가
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    channel.updateName(request.newName());
    channel.updateDescription(request.newDescription());
    repository.save(channel);
    return toResponse(channel);
  }

  @Override
  @Transactional
  public void deleteChannel(UUID channelId) {
    // 연관 Message 삭제
    messageRepository.findAllByChannelId(channelId)
        .forEach(message -> messageRepository.deleteById(message.getId()));

    // 연관 ReadStatus 삭제
    readStatusRepository.findAllByChannelId(channelId)
        .forEach(rs -> readStatusRepository.deleteById(rs.getId()));

    repository.deleteById(channelId);
  }

  private ChannelResponse toResponse(Channel channel) {
    Map<UUID, User> userById = loadParticipants(List.of(channel));
    return channelMapper.toDto(
        channel,
        participantsOf(channel, userById),
        getLastMessageAt(channel));
  }

  private Map<UUID, User> loadParticipants(List<Channel> channels) {
    Set<UUID> participantIds = channels.stream()
        .filter(channel -> channel.getType() == ChannelType.PRIVATE)
        .flatMap(channel -> channel.getAllowedUserList().stream())
        .collect(Collectors.toSet());

    return userRepository.findAllById(participantIds).stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));
  }

  private List<User> participantsOf(Channel channel, Map<UUID, User> userById) {
    if (channel.getType() != ChannelType.PRIVATE) {
      return List.of();
    }

    return channel.getAllowedUserList().stream()
        .map(userId -> {
          User user = userById.get(userId);
          if (user == null) {
            throw new NoSuchElementException("User not found: " + userId);
          }
          return user;
        })
        .toList();
  }

  private Instant getLastMessageAt(Channel channel) {
    return channel.getMessages().stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo)
        .orElse(null);
  }

  private Channel getChannelOrThrow(UUID channelId) {
    return repository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
  }

}
