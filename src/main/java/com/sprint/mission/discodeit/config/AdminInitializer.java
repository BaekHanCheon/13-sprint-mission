package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final String adminUsername;
  private final String adminEmail;
  private final String adminPassword;

  public AdminInitializer(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      @Value("${discodeit.admin.username}") String adminUsername,
      @Value("${discodeit.admin.email}") String adminEmail,
      @Value("${discodeit.admin.password}") String adminPassword
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.adminUsername = adminUsername;
    this.adminEmail = adminEmail;
    this.adminPassword = adminPassword;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    User admin = User.builder()
        .username(adminUsername)
        .email(adminEmail)
        .password(passwordEncoder.encode(adminPassword))
        .role(Role.ADMIN)
        .build();
    userRepository.save(admin);
  }
}
