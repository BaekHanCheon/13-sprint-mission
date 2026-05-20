package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> messageData = new HashMap<>();

    public boolean isPrivateChannelContainsAuthor(Message message, Channel channel){
        if(channel.getType()== ChannelType.PRIVATE){
            if(channel.getAllowedUserList().contains(message.getAuthorId())){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

//    public boolean isManagerChannelContainsAuther(Message message, Channel channel){
//        if(channel.isManagerChannel()){
//            if(channel.getManagerList().contains(message.getAuthorId())){
//                return true;
//            }else{
//                return false;
//            }
//        }else{
//            return true;
//        }
//    }

    private boolean isAccessable(Message message, User user, Channel channel) {
        if (channel.getType() == ChannelType.PRIVATE) {
            if (!isPrivateChannelContainsAuthor(message, channel)) {
                System.out.println("접근 제한 채널 - 생성할 수 없습니다.");
                return false;
            }
        } else if (channel.getType() == ChannelType.MANAGER) {
            if (user.getUserType() != UserType.MANAGER) {
                System.out.println("매니저 전용 채널 - 생성할 수 없습니다.");
                return false;
            }
        }
        return true; // 그 외 일반 채널은 모두 허용
    }

    @Override
    public void createMessage(Message message, User user, Channel channel) {
        if (!isAccessable(message, user, channel)) {

            return;
        }

        messageData.put(message.getId(), message);
        System.out.println("메시지 생성됨");
    }

    @Override
    public void readMessage(UUID id) {
        if(messageData.containsKey(id)){
            System.out.println("read message : ");
            System.out.println(messageData.get(id).toString());
        }else{
            System.out.println("해당 메시지가 없습니다.");
        }

    }

    @Override
    public void readAllMessage() {
        System.out.println("read AllChannels : ");
        System.out.println(messageData.values().toString());
    }

    @Override
    public void modifyMessage(UUID id, String property, String value) {
        Message message = messageData.get(id);

        switch (property){
            case "content" : message.updateContent(value); System.out.println("modified content");break;
        }
        message.updateUpdatedAt();
    }

    @Override
    public void deleteMessage(UUID id) {
        messageData.remove(id);
        System.out.println("deleted message");
    }
}
