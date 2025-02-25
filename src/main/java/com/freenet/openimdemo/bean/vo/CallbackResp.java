package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 回调响应
 */
@Data
public class CallbackResp {
    /**
     * 操作结果，0表示成功
     */
    private Integer actionCode = 0;

    /**
     * 错误码，0表示忽略回调结果
     */
    private Integer errCode = 0;

    /**
     * 错误信息
     */
    private String errMsg = "";

    /**
     * 详细错误信息
     */
    private String errDlt = "";

    /**
     * 下一步执行指令
     */
    private String nextCode = "0";

    /**
     * 群组ID
     */
    private String groupID;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 群组公告
     */
    private String notification;

    /**
     * 群组介绍
     */
    private String introduction;

    /**
     * 群组头像
     */
    private String faceURL;

    /**
     * 群主ID
     */
    private String ownerUserID;

    /**
     * 扩展字段
     */
    private String ex;

    /**
     * 群组状态
     */
    private Integer status;

    /**
     * 创建者ID
     */
    private String creatorUserID;

    /**
     * 群组类型
     */
    private Integer groupType;

    /**
     * 是否需要验证
     */
    private Integer needVerification;

    /**
     * 是否可以查看成员信息
     */
    private Integer lookMemberInfo;

    /**
     * 是否可以添加好友
     */
    private Integer applyMemberFriend;

    /**
     * 创建成功响应
     */
    public static CallbackResp success() {
        return new CallbackResp();
    }
} 