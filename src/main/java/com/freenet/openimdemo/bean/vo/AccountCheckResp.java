package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

@Data
public class AccountCheckResp {
    private Integer errCode;
    private String errMsg;
    private String errDlt;
    private AccountCheckData data;
    
    @Data
    public static class AccountCheckData {
        private List<AccountCheckResult> results;
    }
    
    @Data
    public static class AccountCheckResult {
        private String userID;
        private Integer accountStatus; // 0：已注册，1：未注册
    }
} 