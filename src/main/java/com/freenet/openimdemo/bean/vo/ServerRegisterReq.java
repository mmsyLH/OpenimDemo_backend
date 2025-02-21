package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 服务端注册请求
 */
@Data
public class ServerRegisterReq {
    /**
     * 验证码，固定666666
     */
    private String verifyCode = "666666";
    
    /**
     * 设备ID
     */
    private String deviceID = "";
    
    /**
     * 用户信息
     */
    private UserInfo user;
    
    /**
     * 平台，固定5
     */
    private Integer platform = 5;
    
    @Data
    public static class UserInfo {
        private String faceURL = "";
        private String nickname;
        private String password = "dc483e80a7a0bd9ef71d8cf973673924"; // 固定密码
        private String confirmPassword = "a123456";
        private Integer gender = 0;
        private Integer birth = 0;
        private String phoneNumber;
        private String areaCode = "+86";
        private String email = "";
    }
} 