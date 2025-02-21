package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 用户信息
 */
@Data
public class UserInfo {
    /**
     * 用户ID
     */
    private String userID;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户头像URL
     */
    private String faceURL;
} 