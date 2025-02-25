package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

@Data
public class AIModelReq {
    private String model = "deepseek-r1-distill-qwen-32b";
    private List<Message> messages;
    
    @Data
    public static class Message {
        private String role;
        private String content;
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
} 