package com.freenet.openimdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "openim")
public class OpenIMConfig {
    private Server server = new Server();
    private Account account = new Account();
    private Admin admin = new Admin();

    @Data
    public static class Server {
        private String url;
    }

    @Data
    public static class Account {
        private String url;
    }

    @Data
    public static class Admin {
        private String userId;
        private String secret;
        private String token;
    }
} 