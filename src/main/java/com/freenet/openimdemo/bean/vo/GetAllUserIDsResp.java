package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * 获取所有用户ID响应
 */
@Data
public class GetAllUserIDsResp {
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
    private UserIDsData data;
    
    @Data
    public static class UserIDsData {
        /**
         * 用户总数
         */
        private Integer total;
        
        /**
         * 用户ID列表
         */
        private List<String> userIDs;
    }
} 