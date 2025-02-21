package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * 获取已加入群组响应
 */
@Data
public class GetJoinedGroupResp {
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
    private GroupListData data;
    
    @Data
    public static class GroupListData {
        /**
         * 群组总数
         */
        private Integer total;
        
        /**
         * 群组列表
         */
        private List<CreateGroupResp.GroupDetailInfo> groups;
    }
} 