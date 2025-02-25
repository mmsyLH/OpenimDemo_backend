package com.freenet.openimdemo.bean.callback;

import lombok.Data;
import java.util.List;

/**
 * 群消息回调请求
 */
@Data
public class GroupMsgCallbackReq {
    /**
     * 发送者的唯一标识符
     */
    private String sendID;
    
    /**
     * 回调命令
     */
    private String callbackCommand;
    
    /**
     * 服务端生成的消息ID
     */
    private String serverMsgID;
    
    /**
     * 客户端生成的消息ID
     */
    private String clientMsgID;
    
    /**
     * 操作的唯一标识符
     */
    private String operationID;
    
    /**
     * 发送者的平台ID
     */
    private Integer senderPlatformID;
    
    /**
     * 发送者的昵称
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
     * 消息内容的类型
     */
    private Integer contentType;
    
    /**
     * 消息状态
     */
    private Integer status;
    
    /**
     * 消息发送的时间戳（毫秒）
     */
    private Long sendTime;
    
    /**
     * 消息创建的时间戳（毫秒）
     */
    private Long createTime;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息的序号
     */
    private Long seq;
    
    /**
     * 群组成员ID列表
     */
    private List<String> atUserList;
    
    /**
     * 发送者的头像URL
     */
    private String faceURL;
    
    /**
     * 额外的数据字段
     */
    private String ex;
    
    /**
     * 群组的唯一标识符
     */
    private String groupID;
} 