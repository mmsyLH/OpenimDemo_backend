package com.freenet.openimdemo.bean.callback;

import lombok.Data;
import java.util.List;

@Data
public class CreateGroupCallbackReq {
    private String callbackCommand;
    private String groupID;
    private String groupName;
    private String notification;
    private String introduction;
    private String faceURL;
    private String ownerUserID;
    private Long createTime;
    private Integer memberCount;
    private String ex;
    private Integer status;
    private String creatorUserID;
    private Integer groupType;
    private Integer needVerification;
    private Integer lookMemberInfo;
    private Integer applyMemberFriend;
    private Long notificationUpdateTime;
    private String notificationUserID;
    private List<InitMember> initMemberList;
    
    @Data
    public static class InitMember {
        private String userID;
        private Integer roleLevel;
    }
} 