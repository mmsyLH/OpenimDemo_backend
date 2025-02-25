package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

@Data
public class AccountCheckReq {
    private List<String> checkUserIDs;
} 