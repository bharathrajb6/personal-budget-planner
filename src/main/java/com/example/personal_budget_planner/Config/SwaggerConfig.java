package com.example.personal_budget_planner.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customConfig() {
        return new OpenAPI().info(new Info().title("Personal Budget Planner").title("1.0").description("API Documentation for Personal Budget Planner"));
    }
}
