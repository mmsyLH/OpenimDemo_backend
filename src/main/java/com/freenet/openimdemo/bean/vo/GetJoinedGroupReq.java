package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 获取已加入群组请求
 */
@Data
public class GetJoinedGroupReq {
    /**
     * 用户ID
     */
    private String fromUserID;
    
    /**
     * 分页参数
     */
    private Pagination pagination;
} 