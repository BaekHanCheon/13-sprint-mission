package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);
		/*ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		UserStatusService userStatusService = context.getBean(UserStatusService.class);
		ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
		BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);


		UserResponse user1 = userService.createUser(
				new UserCreateRequest("password", "홍길동", "asdf@naver.com",
						"010-0000-0000",UserType.GENERAL,null)
		);

		UserResponse user2 = userService.createUser(
				new UserCreateRequest("password", "백한천", "qwer@naver.com",
						"010-1111-1111",UserType.GENERAL,new BinaryContentCreateRequest(Path.of("src/main/resources/images/image1.jpg")))
		);


		ChannelResponse channel1 = channelService.createPublicChannel(
				new ChannelPublicCreateRequest("오늘 뭐먹지 채널","공개채널입니다.")
		);

		MessageResponse message1 = messageService.createMessage(
				new MessageCreateRequest("해물파전 어때요",user1.id(),channel1.id(),null)
		);

		List<BinaryContentCreateRequest> binaryContentCreateRequestList = new ArrayList<>();
		binaryContentCreateRequestList.add(new BinaryContentCreateRequest(Path.of("src/main/resources/images/image1.jpg")));
		binaryContentCreateRequestList.add(new BinaryContentCreateRequest(Path.of("src/main/resources/images/image2.jpg")));

		MessageResponse message2 = messageService.createMessage(
				new MessageCreateRequest("해물파전 사진",user1.id(),channel1.id(),binaryContentCreateRequestList)
		);

		messageService.deleteMessage(message2.id());



		ChannelResponse channel2 = channelService.createPrivateChannel(
				new ChannelPrivateCreateRequest(new ArrayList<>(List.of(user1.id(),user2.id())))
		);

		channelService.addAllowedUserList(channel2.id(),user1.id());

		MessageResponse message3 = messageService.createMessage(
				new MessageCreateRequest("이번 멘토링은 토요일 9시에 뵙겠습니다.",user1.id(),channel2.id(),null)
		);

		userService.deleteUser(user1.id());



*/
    }


}
