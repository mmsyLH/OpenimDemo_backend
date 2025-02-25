package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * 导入用户请求
 */
@Data
public class ImportUserReq {
    /**
     * 用户列表
     */
    private List<UserInfo> users;
} 