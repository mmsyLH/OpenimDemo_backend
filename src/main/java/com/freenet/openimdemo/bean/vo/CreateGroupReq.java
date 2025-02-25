package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * 创建群组请求
 */
@Data
public class CreateGroupReq {
    /**
     * 群成员列表
     */
    private List<String> memberUserIDs;
    
    /**
     * 群管理员列表
     */
    private List<String> adminUserIDs;
    
    /**
     * 群主ID
     */
    private String ownerUserID;
    
    /**
     * 群信息
     */
    private GroupInfo groupInfo;
} 