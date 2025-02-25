package com.freenet.openimdemo.bean.vo;

import lombok.Data;

/**
 * 分页参数
 */
@Data
public class Pagination {
    /**
     * 当前页码，从1开始
     */
    private Integer pageNumber;
    
    /**
     * 每页显示数量
     */
    private Integer showNumber;
} 