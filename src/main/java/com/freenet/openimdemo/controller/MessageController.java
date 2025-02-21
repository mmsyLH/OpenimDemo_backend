package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.R;
import org.springframework.web.bind.annotation.*;

/**
 * 消息管理控制器
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    /**
     * 发送消息
     */
    @PostMapping("/sendMsg")
    public R<Object> sendMsg(@RequestBody String requestBody) throws Exception {
        String result = OkHttpUtils.post(ApiConstants.Message.SEND_MSG, requestBody);
        return R.ok(JSONObject.parseObject(result));
    }

    /**
     * 获取聊天记录
     */
    @PostMapping("/getChatLogs")
    public R<Object> getChatLogs(@RequestBody String requestBody) throws Exception {
        String result = OkHttpUtils.post(ApiConstants.Message.GET_CHAT_LOGS, requestBody);
        return R.ok(JSONObject.parseObject(result));
    }
} 