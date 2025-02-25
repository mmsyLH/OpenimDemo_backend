package com.freenet.openimdemo.constants;

/**
 * API接口地址常量类
 */
public class ApiConstants {
    
    /**
     * API服务器基础地址
     */
    private static final String BASE_URL = "http://192.168.80.39:10002";
    
    /**
     * 测试用管理员token
     */
    public static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJpbUFkbWluIiwiUGxhdGZvcm1JRCI6MTAsImV4cCI6MTc0Nzg4MzkzOCwiaWF0IjoxNzQwMTA3OTMzfQ.Do4DD4xm2bWaB96ROjGQm993gJQB6v0z8WESjCcGnlc";
    
    /**
     * 认证相关接口
     */
    public static class Auth {
        /**
         * 获取管理员Token
         */
        public static final String GET_ADMIN_TOKEN = BASE_URL + "/auth/get_admin_token";
        
        /**
         * 获取用户Token
         */
        public static final String GET_USER_TOKEN = BASE_URL + "/auth/get_user_token";
    }

    /**
     * 用户管理相关接口
     */
    public static class User {
        /**
         * 用户注册
         */
        public static final String USER_REGISTER = BASE_URL + "/user/user_register";
        
        /**
         * 获取用户信息
         */
        public static final String GET_USERS_INFO = BASE_URL + "/user/get_users_info";
        
        /**
         * 更新用户信息
         */
        public static final String UPDATE_USER_INFO = BASE_URL + "/user/update_user_info";
        
        /**
         * 获取所有用户ID
         */
        public static final String GET_ALL_USERS_UID = BASE_URL + "/user/get_all_users_uid";
    }

    /**
     * 群组管理相关接口
     */
    public static class Group {
        /**
         * 创建群组
         */
        public static final String CREATE_GROUP = BASE_URL + "/group/create_group";
        
        /**
         * 获取群组信息
         */
        public static final String GET_GROUPS_INFO = BASE_URL + "/group/get_groups_info";
        
        /**
         * 邀请用户加入群组
         */
        public static final String INVITE_USER_TO_GROUP = BASE_URL + "/group/invite_user_to_group";
        
        /**
         * 获取已加入的群组列表
         */
        public static final String GET_JOINED_GROUP_LIST = BASE_URL + "/group/get_joined_group_list";
        
        /**
         * 获取群成员列表
         */
        public static final String GET_GROUP_MEMBER_LIST = BASE_URL + "/group/get_group_member_list";
    }

    /**
     * 会话管理相关接口
     */
    public static class Conversation {
        /**
         * 获取会话列表
         */
        public static final String GET_CONVERSATIONS = BASE_URL + "/conversation/get_conversations";
        
        /**
         * 获取会话信息
         */
        public static final String GET_CONVERSATION = BASE_URL + "/conversation/get_conversation";
    }

    /**
     * 消息管理相关接口
     */
    public static class Message {
        /**
         * 发送消息
         */
        public static final String SEND_MSG = BASE_URL + "/msg/send_msg";
        
        /**
         * 获取聊天记录
         */
        public static final String GET_CHAT_LOGS = BASE_URL + "/msg/get_chat_logs";
    }

    /**
     * Webhook相关接口
     */
    public static class Webhook {
        /**
         * 设置回调地址
         */
        public static final String SET_WEBHOOK = BASE_URL + "/callback/set_webhook";
        
        /**
         * 获取回调配置
         */
        public static final String GET_WEBHOOK = BASE_URL + "/callback/get_webhook";
    }

    /**
     * 账号相关接口
     */
    public static class Account {
        /**
         * 用户注册
         */
        public static final String REGISTER = "http://192.168.80.39:10008/account/register";
        
        /**
         * 用户登录
         */
        public static final String LOGIN = "http://192.168.80.39:10008/account/login";
    }
} 