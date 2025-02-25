package com.freenet.openimdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "human.service")
public class HumanServiceConfig {
    private String userId;
    private String nickname;
    private String faceUrl;
} 