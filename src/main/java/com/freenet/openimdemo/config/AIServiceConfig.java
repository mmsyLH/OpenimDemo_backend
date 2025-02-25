package com.freenet.openimdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.service")
public class AIServiceConfig {
    private String userId;
    private String modelApi;
    private String apiKey;
    private Group group = new Group();

    @Data
    public static class Group {
        private String name;
        private String notification;
        private String introduction;
        private String faceUrl;
    }
} 