package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service @RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository repository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    public boolean isPrivateChannelContainsAuthor(MessageCreateRequest request, Channel channel) {
        if (channel.getType() == ChannelType.PRIVATE) {

            Channel repoChannel = channelRepository.findChannelById(channel.getId()).orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
            return repoChannel.getAllowedUserList().contains(request.authorId());
        }
        return true;
    }

    @Override
    public MessageResponse createMessage(MessageCreateRequest request) {
        Channel channel = getChannelOrThrow(request.channelId());
        User user = getUserOrThrow(request.authorId());
        if (!isAccessable(request, user, channel)) {
            return null;
        }
        Message message = request.toEntity();

        if (request.attachments() != null && !request.attachments().isEmpty()) {
            message.updateAttachmentIds(saveAttachment(message, request.attachments()));        } else {
            log.info("메시지에 첨부파일이 없습니다.");
        }

        repository.createMessage(message);
        log.info("메시지 생성 - {}", message.getContent());
        return MessageResponse.from(message);
    }

    @Override
    public MessageResponse findMessageById(UUID messageId) {
        Message message = getMessageOrThrow(messageId);
        MessageResponse response = MessageResponse.from(message);
        log.info("메시지 조회 - {}", message.getContent());
        return response;
    }

    @Override
    public List<MessageResponse> findAllMessageByChannelId(UUID channelId) {
        System.out.println("메세지 전체조회 -" );

        return repository.findAllMessageByChannelId(channelId).stream().map(MessageResponse::from).toList();
    }

    @Override
    public void updateMessage(MessageUpdateRequest request) {
        Message message = getMessageOrThrow(request.id());

        message.updateContent(request.content());

        message.updateUpdatedAt();
        repository.updateMessage(message);
        log.info("{} 메시지 수정됨", message.getContent());
    }

    @Override
    public void deleteMessage(UUID id) {
        Message message = getMessageOrThrow(id);
        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            List<UUID> attachmentIds = message.getAttachmentIds();
            attachmentIds.forEach(binaryContentRepository::deleteBinaryContent);
            log.info("첨부파일 삭제");
        }
        repository.deleteMessage(id);
        System.out.println("메세지 삭제됨");
    }

    private Message getMessageOrThrow(UUID messageId) {
        return repository.findMessageById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메세지가 없습니다."));
    }

    private Channel getChannelOrThrow(UUID channelId) {
        return channelRepository.findChannelById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
    }

    private User getUserOrThrow(UUID userId){
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    }

    private boolean isAccessable(MessageCreateRequest request, User user, Channel channel) {
        if (channel.getType() == ChannelType.PRIVATE) {
            if (!isPrivateChannelContainsAuthor(request, channel)) {
                System.out.println("접근 제한 채널 - 생성할 수 없습니다.");
                return false;
            }
        } else if (channel.getType() == ChannelType.MANAGER) {
            if (user.getUserType() != UserType.MANAGER) {
                System.out.println("매니저 전용 채널 - 생성할 수 없습니다.");
                return false;
            }
        }
        return true;
    }

    private List<UUID> saveAttachment(Message message, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        List<UUID> attachmentIds = new ArrayList<>();

        if (binaryContentCreateRequests != null && !binaryContentCreateRequests.isEmpty()) {
            binaryContentCreateRequests.forEach(request -> {
                String savedFileName = binaryContentRepository.saveFile(request.filePath());
                BinaryContent binaryContent = new BinaryContent(savedFileName, message.getAuthorId(), message.getId());

                log.info("파일 {} 저장됨", savedFileName);
                binaryContentRepository.createBinaryContent(binaryContent);
                attachmentIds.add(binaryContent.getId());
            });
        }

        return attachmentIds;
    }
}
