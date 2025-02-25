package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * 邀请用户进群请求
 */
@Data
public class InviteGroupReq {
    /**
     * 群ID
     */
    private String groupID;
    
    /**
     * 被邀请的用户ID列表
     */
    private List<String> invitedUserIDs;
    
    /**
     * 邀请说明
     */
    private String reason;
} 