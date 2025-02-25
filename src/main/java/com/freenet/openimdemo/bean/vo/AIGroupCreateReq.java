package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * AI群聊创建请求
 */
@Data
public class AIGroupCreateReq {
    /**
     * 群成员ID列表
     */
    private List<String> memberUserIDs;
    
    /**
     * 群主ID，固定为imAdmin
     */
    private String ownerUserID;
    
    /**
     * 群组信息
     */
    private GroupInfo groupInfo;
    
    @Data
    public static class GroupInfo {
        /**
         * 群ID，使用时间戳生成
         */
        private String groupID;
        
        /**
         * 群名称
         */
        private String groupName = "智能客服";
//        private String groupName = "智能客服";

        /**
         * 群公告
         */
        private String notification = "";
//        private String notification = "智能客服为您服务";

        /**
         * 群介绍
         */
        private String introduction = "智能客服介绍";
        
        /**
         * 群头像
         */
        private String faceURL = "https://lhwaimai.oss-cn-beijing.aliyuncs.com/4aa57d12-feaf-4fb1-9794-4626a455bfd3.jpg";
        
        /**
         * 扩展字段
         */
        private String ex = "ex";
        
        /**
         * 群类型
         */
        private Integer groupType = 2;
        
        /**
         * 是否需要验证
         */
        private Integer needVerification = 0;
        
        /**
         * 是否可以查看成员信息
         */
        private Integer lookMemberInfo = 0;
        
        /**
         * 是否允许成员加好友
         */
        private Integer applyMemberFriend = 0;
    }
} 