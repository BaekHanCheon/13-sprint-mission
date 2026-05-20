package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        //JFC 서비스 생성
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        //유저 생성
        User user1 = new User("asdf", "닉네임","asdf@asdf.com", "010-1111-2222", UserType.GENERAL);
        User user2 = new User("asdf2", "닉네임2","asdf@asdf.com2", "010-1111-3333", UserType.MANAGER);
        User user3 = new User("asdf3", "닉네임2","asdf@asdf.com2", "010-1111-4444", UserType.GENERAL); //중복이메일 유저
        User user4 = new User("asdf4", "닉네임4","asdf@asdf.com4", "010-1111-3333", UserType.GENERAL); //중복 핸드폰번호 유저

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);//중복이메일 생성불가
        userService.createUser(user4);//중복휴대폰번호 생성불가

        user4 = new User("asdf4", "닉네임4","asdf@asdf.com4", "010-1111-4444", UserType.GENERAL);
        userService.createUser(user4);

        //유저 조회
        userService.readUser(user1.getId());
        userService.readAllUser();

        //유저 정보 수정
        userService.modifyUser(user1.getId(),"email","zxcv@naver.com");
        userService.modifyUser(user2.getId(),"password","zxcvzxcv");
        userService.readAllUser();

        System.out.println("=================================================================");

        //채널 생성
        Channel channel1 = new Channel(ChannelType.PUBLIC,"일반 채널","오늘 뭐먹지 채널입니다. 모두가 메시지 생성, 접근이 가능합니다.");
        channelService.createChannel(channel1);
        Channel channel2 = new Channel(ChannelType.PRIVATE,"팀 채널","4팀 채널입니다. 지정 사용자, 매니저만 메시지 생성, 접근 가능합니다.");
        channelService.createChannel(channel2);
        Channel channel3 = new Channel(ChannelType.MANAGER,"공지 채널","행정-공지 게시판입니다. 매니저만 메시지 생성이 가능하고, 모든 사용자가 접근 가능합니다.");
        channelService.createChannel(channel3);

        //일반(public) 채널메시지 생성
        Message message1 = new Message("user1가 channel1(일반)에 생성한 메세지 입니다.", user1.getId(), channel1.getId());
        messageService.createMessage(message1, user1 ,channel1); //생성 성공
        messageService.readMessage(message1.getId()); //조회 성공

        //팀(private) 채널메시지 생성
        Message message2 = new Message("user1가 channel2(팀)에 생성한 메세지 입니다.", user1.getId(), channel2.getId());
        messageService.createMessage(message2, user1 ,channel2); //생성 실패
        messageService.readMessage(message2.getId()); //조회 실패

        //프라이빗 채널 접근 허용목록 등록
        channelService.addAllowedUserList(channel2.getId(),user1.getId());

        messageService.createMessage(message2, user1 ,channel2); //생성 성공
        messageService.readMessage(message2.getId()); //조회 성공

        //공지(manager) 채널메시지 생성
        Message message3 = new Message("user1(일반)가 channel3(매니저)에 생성한 메세지 입니다.", user1.getId(), channel3.getId());
        messageService.createMessage(message3, user1 ,channel3); //실패
        messageService.readMessage(message3.getId()); //실패

        Message message4 = new Message("user2(매니저)가 channel3(매니저)에 생성한 메세지 입니다.", user2.getId(), channel3.getId());
        messageService.createMessage(message4, user2 ,channel3); //성공
        messageService.readMessage(message4.getId()); //성공

        System.out.println("=================================================================");

        //메시지 수정
        messageService.modifyMessage(message1.getId(),"content","수정된 메세지입니다.");
        messageService.readMessage(message1.getId());


        //유저 삭제
        userService.deleteUser(user1.getId());
        userService.readAllUser();



    }
}
