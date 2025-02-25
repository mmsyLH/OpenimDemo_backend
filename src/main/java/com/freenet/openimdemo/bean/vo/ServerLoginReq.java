package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 服务端登录请求
 */
@Data
public class ServerLoginReq {
    /**
     * 固定登录用户信息
     */
    public static class LoginUser1 {
        /**
         * 固定密码
         */
        public static final String PASSWORD = "dc483e80a7a0bd9ef71d8cf973673924";
        
        /**
         * 区号
         */
        public static final String AREA_CODE = "+86";
        
        /**
         * 验证码
         */
        public static final String VERIFY_CODE = "";
        
        /**
         * 邮箱
         */
        public static final String EMAIL = "";
        
        /**
         * 设备ID
         */
        public static final String DEVICE_ID = "";
        
        /**
         * 平台
         */
        public static final Integer PLATFORM = 5;
        
        /**
         * 账号
         */
        public static final String ACCOUNT = "";
    }
    
    /**
     * 手机号
     */
    private String phoneNumber;
    
    /**
     * 固定密码
     */
    private String password = LoginUser1.PASSWORD;
    
    /**
     * 区号
     */
    private String areaCode = LoginUser1.AREA_CODE;
    
    /**
     * 验证码
     */
    private String verifyCode = LoginUser1.VERIFY_CODE;
    
    /**
     * 邮箱
     */
    private String email = LoginUser1.EMAIL;
    
    /**
     * 设备ID
     */
    private String deviceID = LoginUser1.DEVICE_ID;
    
    /**
     * 平台
     */
    private Integer platform = LoginUser1.PLATFORM;
    
    /**
     * 账号
     */
    private String account = LoginUser1.ACCOUNT;
} 