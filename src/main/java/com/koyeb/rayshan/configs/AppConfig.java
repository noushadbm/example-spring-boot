package com.koyeb.rayshan.configs;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfig {
    @Value("${spring.r2dbc.username}")
    private String neonDbUsername;

    @Value("${app.config.security.jwt-secret}")
    private String jwtSecret;
}
