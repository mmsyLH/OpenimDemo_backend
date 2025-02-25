package com.freenet.openimdemo.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * 群消息回调请求
 */
@Data
public class GroupMsgCallbackReq {
    /**
     * 发送者ID
     */
    private String sendID;
    
    /**
     * 回调命令
     */
    private String callbackCommand;
    
    /**
     * 服务端消息ID
     */
    private String serverMsgID;
    
    /**
     * 客户端消息ID
     */
    private String clientMsgID;
    
    /**
     * 操作ID
     */
    private String operationID;
    
    /**
     * 发送者平台ID
     */
    private Integer senderPlatformID;
    
    /**
     * 发送者昵称
     */
    private String senderNickname;
    
    /**
     * 会话类型
     */
    private Integer sessionType;
    
    /**
     * 消息来源
     */
    private Integer msgFrom;
    
    /**
     * 内容类型
     */
    private Integer contentType;
    
    /**
     * 消息状态
     */
    private Integer status;
    
    /**
     * 发送时间
     */
    private Long sendTime;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息序号
     */
    private Long seq;
    
    /**
     * @用户列表
     */
    private List<String> atUserList;
    
    /**
     * 发送者头像
     */
    private String faceURL;
    
    /**
     * 扩展字段
     */
    private String ex;
    
    /**
     * 群组ID
     */
    private String groupID;
} 