package KSUTech.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class Swagger2Config {

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("v1-definition")
                .pathsToMatch("/api/**")
                .build();
    }
    @Bean
    public OpenAPI TechLabOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("KSUTechLab API")
                        .description("KSUTechLab API 명세서 입니다.")
                        .version("v0.0.1"));
    }
}
