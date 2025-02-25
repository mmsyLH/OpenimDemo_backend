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
    private String nextCode = "";
} 