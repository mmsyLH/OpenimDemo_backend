package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 群组信息
 */
@Data
public class GroupInfo {
    /**
     * 群ID
     */
    private String groupID;
    
    /**
     * 群名称
     */
    private String groupName;
    
    /**
     * 群公告
     */
    private String notification;
    
    /**
     * 群介绍
     */
    private String introduction;
    
    /**
     * 群头像地址
     */
    private String faceURL;
    
    /**
     * 群扩展字段
     */
    private String ex;
    
    /**
     * 群类型，固定为2
     */
    private Integer groupType = 2;
    
    /**
     * 进群是否需要验证
     */
    private Integer needVerification;
    
    /**
     * 能否查看其他群成员信息
     */
    private Integer lookMemberInfo;
    
    /**
     * 群成员是否允许添加好友
     */
    private Integer applyMemberFriend;
} 