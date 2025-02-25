package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 获取用户token请求
 */
@Data
public class GetTokenReq implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户登录时的终端类型，取值为1-9
     */
    private Integer platformID;
    private String secret;

    /**
     * 用户ID
     */
    private String userID;
} 