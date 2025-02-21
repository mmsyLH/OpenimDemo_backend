package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 获取所有用户ID请求
 */
@Data
public class GetAllUserIDsReq {
    /**
     * 分页参数
     */
    private Pagination pagination;
} 