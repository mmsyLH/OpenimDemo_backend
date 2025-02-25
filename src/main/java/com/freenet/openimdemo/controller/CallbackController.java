package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.GroupMsgCallbackReq;
import com.freenet.openimdemo.bean.vo.CallbackResp;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import com.freenet.openimdemo.bean.vo.AIModelReq;
import com.freenet.openimdemo.bean.vo.AIModelResp;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.Utils;

/**
 * 回调接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/callback")
@CrossOrigin(origins = "http://localhost:3003", allowCredentials = "true")
public class CallbackController {

    private static final String AI_USER_ID = "10086";
    private static final String AI_MODEL_API = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String AI_API_KEY = "sk-1d55ab1212d04341a03844a63b1ee041";

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
            
            // 1. 过滤系统消息和非文本消息
            if (callbackReq.getContentType() != 101) { // 101表示文本消息
                return new CallbackResp();
            }
            
            // 2. 过滤AI自己发送的消息
            if (AI_USER_ID.equals(callbackReq.getSendID())) {
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
                    if (AI_USER_ID.equals(members.getJSONObject(i).getString("userID"))) {
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
                .url(AI_MODEL_API)
                .addHeader("Authorization", AI_API_KEY)
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
        req.put("sendID", AI_USER_ID);
        req.put("groupID", groupID);
        req.put("senderNickname", "智能客服");
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
} 