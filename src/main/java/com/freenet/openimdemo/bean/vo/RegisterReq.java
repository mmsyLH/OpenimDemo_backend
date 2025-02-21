package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 注册请求参数
 */
@Data
public class RegisterReq {
    /**
     * 手机号
     */
    private String phoneNumber;
    
    /**
     * 用户昵称
     */
    private String nickname;
} 