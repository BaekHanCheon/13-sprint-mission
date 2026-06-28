package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI discodeitOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Discodeit API")
            .description("Discodeit 서비스의 REST API 문서입니다.")
            .version("v1.0.0")
            .contact(new Contact()
                .name("Sprint Mission")
            ));
  }
}
