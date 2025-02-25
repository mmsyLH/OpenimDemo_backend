package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 管理员令牌响应
 *
 * @author luohan
 * @date 2025/02/21
 */
@Data
public class TokenResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer errCode;
    private String errMsg;
    private String errDlt;
    private TokenData data;

    @Data
    public static class TokenData implements Serializable {
        private static final long serialVersionUID = 10086L;
        private String token;
        private Long expireTimeSeconds;
    }
} 