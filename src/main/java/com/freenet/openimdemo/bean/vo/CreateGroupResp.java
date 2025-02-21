package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 创建群组响应
 */
@Data
public class CreateGroupResp {
    /**
     * 错误码
     */
    private Integer errCode;
    
    /**
     * 错误信息
     */
    private String errMsg;
    
    /**
     * 错误详情
     */
    private String errDlt;
    
    /**
     * 响应数据
     */
    private GroupRespData data;
    
    @Data
    public static class GroupRespData {
        /**
         * 群组信息
         */
        private GroupDetailInfo groupInfo;
    }
    
    @Data
    public static class GroupDetailInfo extends GroupInfo {
        private String ownerUserID;
        private Long createTime;
        private Integer memberCount;
        private Integer status;
        private String creatorUserID;
        private Long notificationUpdateTime;
        private String notificationUserID;
    }
} 