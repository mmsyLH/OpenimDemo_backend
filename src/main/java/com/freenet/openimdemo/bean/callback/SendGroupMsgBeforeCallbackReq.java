package com.freenet.openimdemo.bean.callback;

import lombok.Data;
import java.util.List;

/**
 * 发送群消息前的回调请求
 */
@Data
public class SendGroupMsgBeforeCallbackReq {
    /**
     * 发送者ID
     */
    private String sendID;
    
    /**
     * 回调命令
     */
    private String callbackCommand;
    
    /**
     * 服务器消息ID
     */
    private String serverMsgID;
    
    /**
     * 客户端消息ID
     */
    private String clientMsgID;
    
    /**
     * 操作ID，用于全局链路追踪
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
     * 消息来源，1表示来自用户，2表示来自群组
     */
    private Integer msgFrom;
    
    /**
     * 消息类型，101表示文本消息
     */
    private Integer contentType;
    
    /**
     * 消息状态
     */
    private Integer status;
    
    /**
     * 发送时间戳（毫秒）
     */
    private Long sendTime;
    
    /**
     * 创建时间戳（毫秒）
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
     * 发送者头像URL
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