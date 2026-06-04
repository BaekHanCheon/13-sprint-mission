package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);

		MessageRepository messageRepository = new FileMessageRepository();
		UserRepository userRepository = new FileUserRepository();
		ChannelRepository channelRepository = new FileChannelRepository();

		//Basic
		UserService userService = new BasicUserService(userRepository);
		MessageService messageService = new BasicMessageService(messageRepository, channelRepository);
		ChannelService channelService = new BasicChannelService(channelRepository);

		//수정 탬플릿
		User user1 = setupUser(userService);
		Channel channel1 = setupChannel(channelService);
		Message message1 = messageCreateTest(messageService, channel1 ,user1);

		//------------------------------------------------------------------------------

		//파일 서비스 생성
		//FileUserService userService = new FileUserService();
		//FileChannelService channelService = new FileChannelService();
		//FileMessageService messageService = new FileMessageService();

		//Jcf 서비스 생성
		//JCFUserService userService = new JCFUserService();
		//JCFChannelService channelService = new JCFChannelService();
		//JCFMessageService messageService = new JCFMessageService();

		//유저 생성
		//User user1 = new User("asdf", "닉네임","asdf@asdf.com", "010-1111-2222", UserType.GENERAL);
		User user2 = new User("asdf2", "닉네임2","asdf@asdf.com2", "010-1111-3333", UserType.MANAGER);
		User user3 = new User("asdf3", "닉네임2","asdf@asdf.com2", "010-1111-4444", UserType.GENERAL); //중복이메일 유저
		User user4 = new User("asdf4", "닉네임4","asdf@asdf.com4", "010-1111-3333", UserType.GENERAL); //중복 핸드폰번호 유저

		userService.createUser(user1);
		userService.createUser(user2);
		//userService.createUser(user3);//중복이메일 생성불가
		//userService.createUser(user4);//중복휴대폰번호 생성불가

		user4 = new User("asdf4", "닉네임4","asdf@asdf.com4", "010-1111-4444", UserType.GENERAL);
		userService.createUser(user4);

		//유저 조회
		userService.findUserById(user1.getId());
		userService.findAllUser();

		//유저 정보 수정
		userService.updateUser(user1.getId(),"email","zxcv@naver.com");
		userService.updateUser(user2.getId(),"password","zxcvzxcv");
		userService.findAllUser();

		System.out.println("=================================================================");

		//채널 생성
		//Channel channel1 = new Channel(ChannelType.PUBLIC,"일반 채널","오늘 뭐먹지 채널입니다. 모두가 메시지 생성, 접근이 가능합니다.");
		//channelService.createChannel(channel1);
		Channel channel2 = new Channel(ChannelType.PRIVATE,"팀 채널","4팀 채널입니다. 지정 사용자, 매니저만 메시지 생성, 접근 가능합니다.");
		channelService.createChannel(channel2);
		Channel channel3 = new Channel(ChannelType.MANAGER,"공지 채널","행정-공지 게시판입니다. 매니저만 메시지 생성이 가능하고, 모든 사용자가 접근 가능합니다.");
		channelService.createChannel(channel3);

		channelService.findAllChannel();

		//일반(public) 채널메시지 생성
		//Message message1 = new Message("user1가 channel1(일반)에 생성한 메세지 입니다.", user1.getId(), channel1.getId());
		//messageService.createMessage(message1, user1 ,channel1); //생성 성공
		//messageService.findMessageById(message1.getId()); //조회 성공

		//팀(private) 채널메시지 생성
		Message message2 = new Message("user1가 channel2(팀)에 생성한 메세지 입니다.", user1.getId(), channel2.getId());
		//messageService.createMessage(message2, user1 ,channel2); //생성 실패
		//messageService.findMessageById(message2.getId()); //조회 실패

		//프라이빗 채널 접근 허용목록 등록
		channelService.addAllowedUserList(channel2.getId(),user1.getId());

		messageService.createMessage(message2, user1 ,channel2); //생성 성공
		messageService.findMessageById(message2.getId()); //조회 성공

		//공지(manager) 채널메시지 생성
		Message message3 = new Message("user1(일반)가 channel3(매니저)에 생성한 메세지 입니다.", user1.getId(), channel3.getId());
		//messageService.createMessage(message3, user1 ,channel3); //실패
		//messageService.findMessageById(message3.getId()); //실패

		Message message4 = new Message("user2(매니저)가 channel3(매니저)에 생성한 메세지 입니다.", user2.getId(), channel3.getId());
		messageService.createMessage(message4, user2 ,channel3); //성공
		messageService.findMessageById(message4.getId()); //성공

		System.out.println("=================================================================");

		//메시지 수정
		messageService.updateMessage(message1.getId(),"content","수정된 메세지입니다.");
		messageService.findMessageById(message1.getId());


		//유저 삭제
		userService.deleteUser(user1.getId());
		userService.findAllUser();

	}

	static User setupUser(UserService userService) {
		User user = new User("woody1234", "woody","woody@codeit.com", "010-1111-2222", UserType.GENERAL);
		userService.createUser(user);
		return user;
	}

	static Channel setupChannel(ChannelService channelService) {
		Channel channel = new Channel(ChannelType.PUBLIC,"일반 채널","오늘 뭐먹지 채널입니다. 모두가 메시지 생성, 접근이 가능합니다.");
		channelService.createChannel(channel);
		return channel;
	}

	static Message messageCreateTest(MessageService messageService, Channel channel, User author) {
		Message message = new Message("생성한 메세지 입니다.", author.getId(), channel.getId());
		messageService.createMessage(message, author ,channel);
		System.out.println("메시지 생성: " + message.getId());
		return message;
	}

}
