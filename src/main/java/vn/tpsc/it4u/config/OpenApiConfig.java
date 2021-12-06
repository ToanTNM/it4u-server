package vn.tpsc.it4u.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        // info
        .info(new Info().title("IT4U Application API")
            .description("IT4U Application API")
            .contact(new Contact()
                .email("dev@tpsc.vn")
                .name("Dev")
                .url("https://tpsc.vn"))
            .license(new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
            .version("1.0.0"));
  }
}
