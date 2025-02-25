package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.R;
import org.springframework.web.bind.annotation.*;

/**
 * 会话管理控制器
 */
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    /**
     * 获取会话列表
     */
    @PostMapping("/getConversations")
    public R<Object> getConversations(@RequestBody String requestBody) throws Exception {
        String result = OkHttpUtils.post(ApiConstants.Conversation.GET_CONVERSATIONS, requestBody);
        return R.ok(JSONObject.parseObject(result));
    }

    /**
     * 获取会话信息
     */
    @PostMapping("/getConversation")
    public R<Object> getConversation(@RequestBody String requestBody) throws Exception {
        String result = OkHttpUtils.post(ApiConstants.Conversation.GET_CONVERSATION, requestBody);
        return R.ok(JSONObject.parseObject(result));
    }
} 