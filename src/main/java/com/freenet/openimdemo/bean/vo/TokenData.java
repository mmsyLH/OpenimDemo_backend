package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 令牌数据
 *
 * @author luohan
 * @date 2025/02/25
 */
@Data
public class TokenData implements Serializable {
    private static final long serialVersionUID = 10086L;
    private String token;
    private Long expireTimeSeconds;
} 