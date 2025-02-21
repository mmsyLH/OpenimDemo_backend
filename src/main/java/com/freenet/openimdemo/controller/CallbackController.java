package com.freenet.openimdemo.controller;

import com.freenet.openimdemo.bean.vo.GroupMsgCallbackReq;
import com.freenet.openimdemo.bean.vo.CallbackResp;
import com.freenet.openimdemo.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 回调接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/callback")
@CrossOrigin(origins = "http://localhost:3003", allowCredentials = "true")
public class CallbackController {

    /**
     * 群消息发送后的回调
     *
     * @param callbackReq 回调请求参数
     * @return 回调响应
     */
    @PostMapping("/callbackAfterSendGroupMsgCommand")
    public CallbackResp afterSendGroupMsg(@RequestBody GroupMsgCallbackReq callbackReq) {
        try {
            // 记录回调信息
            log.info("收到群消息回调: groupID={}, sendID={}, content={}", 
                    callbackReq.getGroupID(), 
                    callbackReq.getSendID(), 
                    callbackReq.getContent());
            
            // TODO: 这里可以添加自己的业务逻辑
            // 例如：
            // 1. 保存消息到数据库
            // 2. 消息审核
            // 3. 触发其他业务流程
            // 4. 统计分析等
            
            // 返回成功响应
            CallbackResp resp = new CallbackResp();
            return resp;
            
        } catch (Exception e) {
            log.error("处理群消息回调异常", e);
            
            // 返回错误响应
            CallbackResp resp = new CallbackResp();
            resp.setActionCode(1);
            resp.setErrCode(500);
            resp.setErrMsg("处理回调失败");
            resp.setErrDlt(e.getMessage());
            return resp;
        }
    }
} 