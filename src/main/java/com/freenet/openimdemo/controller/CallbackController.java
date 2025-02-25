package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.*;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.Utils;
import com.freenet.openimdemo.bean.callback.CreateGroupCallbackReq;
import com.freenet.openimdemo.config.AIServiceConfig;
import com.freenet.openimdemo.config.HumanServiceConfig;
import java.util.Collections;
import com.freenet.openimdemo.bean.callback.SendGroupMsgBeforeCallbackReq;

/**
 * 回调接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/callback")
@CrossOrigin(origins = "${cors.allowed-origins}", allowCredentials = "true")
public class CallbackController {

    private final AIServiceConfig aiConfig;
    private final HumanServiceConfig humanConfig;
    
    public CallbackController(AIServiceConfig aiConfig, HumanServiceConfig humanConfig) {
        this.aiConfig = aiConfig;
        this.humanConfig = humanConfig;
    }

    /**
     * 群消息发送后的回调
     *
     * @param callbackReq 回调请求参数
     * @return 回调响应
     */
    @PostMapping("/callbackAfterSendGroupMsgCommand")
    public CallbackResp afterSendGroupMsg(@RequestBody GroupMsgCallbackReq callbackReq) {
        try {
            log.info("收到群消息回调: {}", callbackReq);
            log.info("收到群消息{}", callbackReq.getContent());
            
            // 1. 过滤系统消息和非文本消息
            if (callbackReq.getContentType() != 101) {
                return new CallbackResp();
            }
            
            // 2. 过滤AI和人工客服发送的消息
            if (aiConfig.getUserId().equals(callbackReq.getSendID()) || 
                humanConfig.getUserId().equals(callbackReq.getSendID())) {
                return new CallbackResp();
            }
            
            // 3. 获取群成员列表，判断是否是AI客服群聊
            if (isAIServiceGroup(callbackReq.getGroupID())) {
                // 4. 调用大模型API
                String aiResponse = callAIModel(callbackReq.getContent());
                
                // 5. 发送AI回复消息
                sendAIResponse(callbackReq.getGroupID(), aiResponse);
            }
            
            return new CallbackResp();
        } catch (Exception e) {
            log.error("处理群消息回调异常", e);
            CallbackResp resp = new CallbackResp();
            resp.setActionCode(1);
            resp.setErrCode(500);
            resp.setErrMsg("处理回调失败");
            resp.setErrDlt(e.getMessage());
            return resp;
        }
    }
    
    /**
     * 判断是否是AI服务群聊
     */
    private boolean isAIServiceGroup(String groupID) throws Exception {
        // 构建获取群成员请求
        JSONObject req = new JSONObject();
        req.put("groupID", groupID);
        req.put("pagination", new JSONObject()
                .fluentPut("pageNumber", 1)
                .fluentPut("showNumber", 100));
        
        // 发送请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Group.GET_GROUP_MEMBER_LIST)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .post(okhttp3.RequestBody.create(req.toJSONString(), OkHttpUtils.JSON))
                .build();
        
        String result = OkHttpUtils.post(request);
        JSONObject resp = JSONObject.parseObject(result);
        
        // 判断是否是双人群聊且包含AI客服
        if (resp.getInteger("errCode") == 0) {
            JSONObject data = resp.getJSONObject("data");
            if (data.getInteger("total") == 2) {
                JSONArray members = data.getJSONArray("members");
                for (int i = 0; i < members.size(); i++) {
                    if (aiConfig.getUserId().equals(members.getJSONObject(i).getString("userID"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 调用大模型API
     */
    private String callAIModel(String userMessage) throws Exception {
        // 构建请求体
        AIModelReq aiReq = new AIModelReq();
        List<AIModelReq.Message> messages = new ArrayList<>();
        messages.add(new AIModelReq.Message("user", userMessage));
        aiReq.setMessages(messages);
        
        // 发送请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(aiConfig.getModelApi())
                .addHeader("Authorization", aiConfig.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(okhttp3.RequestBody.create(JSON.toJSONString(aiReq), OkHttpUtils.JSON))
                .build();
        
        String result = OkHttpUtils.post(request);
        AIModelResp aiResp = JSONObject.parseObject(result, AIModelResp.class);
        
        return aiResp.getChoices().get(0).getMessage().getContent();
    }
    
    /**
     * 发送AI回复消息
     */
    private void sendAIResponse(String groupID, String content) throws Exception {
        JSONObject req = new JSONObject();
        req.put("sendID", aiConfig.getUserId());
        req.put("groupID", groupID);
        req.put("senderNickname", aiConfig.getGroup().getName());
        req.put("content", new JSONObject().fluentPut("content", content));
        req.put("contentType", 101);
        req.put("sessionType", 3);
        
        // 发送消息
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Message.SEND_MSG)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .post(okhttp3.RequestBody.create(req.toJSONString(), OkHttpUtils.JSON))
                .build();
        
        OkHttpUtils.post(request);
    }

    /**
     * 群组创建前的回调
     *
     * @param callbackReq 回调请求参数
     * @return 回调响应
     */
    @PostMapping("/callbackBeforeCreateGroupCommand")
    public CallbackResp beforeCreateGroup(@RequestBody CreateGroupCallbackReq callbackReq) {
        try {
            log.info("收到创建群组前回调: {}", callbackReq);
            
            // 1. 判断是否是AI客服群聊
            if (aiConfig.getUserId().equals(callbackReq.getOwnerUserID())) {
                // 2. 修改群组信息
                CallbackResp resp = new CallbackResp();
                resp.setActionCode(0);
                resp.setNextCode("0");
                
                // 设置群组信息
                resp.setGroupName(aiConfig.getGroup().getName());
                resp.setNotification(aiConfig.getGroup().getNotification());
                resp.setIntroduction(aiConfig.getGroup().getIntroduction());
                resp.setFaceURL(aiConfig.getGroup().getFaceUrl());
                resp.setGroupType(2);
                resp.setNeedVerification(0);
                resp.setLookMemberInfo(1);
                resp.setApplyMemberFriend(0);
                
                return resp;
            }
            
            // 3. 其他类型的群聊
            // 这里可以添加其他类型群聊的验证逻辑
            // 例如：
            // - 验证群名是否合法
            // - 验证群成员数量是否合法
            // - 验证创建者权限
            // - 等等
            
            // 4. 允许创建
            return new CallbackResp();
            
        } catch (Exception e) {
            log.error("处理创建群组前回调异常", e);
            CallbackResp resp = new CallbackResp();
            resp.setActionCode(1);
            resp.setErrCode(5001);
            resp.setErrMsg("创建群组失败");
            resp.setErrDlt(e.getMessage());
            resp.setNextCode("1");
            return resp;
        }
    }

    /**
     * 群组创建后的回调
     *
     * @param callbackReq 回调请求参数
     * @return 回调响应
     */
    @PostMapping("/callbackAfterCreateGroupCommand")
    public CallbackResp afterCreateGroup(@RequestBody CreateGroupCallbackReq callbackReq) {
        try {
            log.info("收到创建群组回调: {}", callbackReq);
            
//            // 1. 判断是否是AI客服群聊
//            if (AI_USER_ID.equals(callbackReq.getOwnerUserID())) {
//                // 2. 发送欢迎消息
//                String welcomeMsg = "您好！我是智能客服，请问有什么可以帮您？";
//                sendAIResponse(callbackReq.getGroupID(), welcomeMsg);
//
//                // 3. 更新群组信息（如果需要）
//                // updateGroupInfo(callbackReq.getGroupID());
//            }
//
            return new CallbackResp();
        } catch (Exception e) {
            log.error("处理创建群组回调异常", e);
            CallbackResp resp = new CallbackResp();
            resp.setActionCode(1);
            resp.setErrCode(500);
            resp.setErrMsg("处理回调失败");
            resp.setErrDlt(e.getMessage());
            return resp;
        }
    }

    /**
     * 发送群消息前的回调
     *
     * @param callbackReq 回调请求参数
     * @return 回调响应
     */
    @PostMapping("/callbackBeforeSendGroupMsgCommand")
    public CallbackResp2 beforeSendGroupMsg(@RequestBody SendGroupMsgBeforeCallbackReq callbackReq) {
        try {
            log.info("收到发送群消息前回调: {}", callbackReq);
            if (callbackReq.getContentType() == 1514||callbackReq.getContentType() == 1515||callbackReq.getContentType() == 1501) {
                CallbackResp2 resp = new CallbackResp2();
                resp.setActionCode(0);
                resp.setErrCode(5001);
                resp.setErrMsg("系统消息不显示");
                resp.setErrDlt("系统消息不显示");
                resp.setNextCode(1);
                log.info("收到发送群消息前回调: {}", resp);
                return resp;
            }
            // 1. 过滤系统消息和非文本消息
            if (callbackReq.getContentType() != 101) {
//                return CallbackResp.success();
            }

            // 2. 过滤AI和人工客服发送的消息
            if (aiConfig.getUserId().equals(callbackReq.getSendID()) ||
                humanConfig.getUserId().equals(callbackReq.getSendID())) {
//                return CallbackResp.success();
            }

            // 3. 检查消息内容是否合法（示例：检查敏感词）
            if (containsSensitiveWords(callbackReq.getContent())) {
                CallbackResp2 resp = new CallbackResp2();
                resp.setActionCode(1);
                resp.setErrCode(5001);
                resp.setErrMsg("消息包含敏感词");
                resp.setErrDlt("请文明发言");
                resp.setNextCode(1);
                return resp;
            }

            CallbackResp2 resp = new CallbackResp2();
            resp.setActionCode(0);
            resp.setErrCode(0);
            resp.setErrMsg("");
            resp.setErrDlt("");
            resp.setNextCode(0);
            // 4. 允许发送消息
            return resp;

        } catch (Exception e) {
            log.error("处理发送群消息前回调异常", e);
            CallbackResp2 resp = new CallbackResp2();
            resp.setActionCode(1);
            resp.setErrCode(5001);
            resp.setErrMsg("处理回调失败");
            resp.setErrDlt(e.getMessage());
            resp.setNextCode(1);
            return resp;
        }
    }

    /**
     * 检查是否包含敏感词（示例方法）
     */
    private boolean containsSensitiveWords(String content) {
        // 这里可以实现具体的敏感词检查逻辑
        // 例如：使用敏感词库、调用第三方服务等
        return false;
    }
} 