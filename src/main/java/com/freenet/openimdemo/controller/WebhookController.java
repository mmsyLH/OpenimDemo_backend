package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.callback.CallbackResp;
import com.freenet.openimdemo.bean.callback.GroupMsgCallbackReq;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Webhook管理控制器
 */
@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    /**
     * 设置回调地址
     */
    @PostMapping("/setWebhook")
    public R<Object> setWebhook(@RequestBody String requestBody) throws Exception {
        String result = OkHttpUtils.post(ApiConstants.Webhook.SET_WEBHOOK, requestBody);
        return R.ok(JSONObject.parseObject(result));
    }

    /**
     * 获取回调配置
     */
    @PostMapping("/getWebhook")
    public R<Object> getWebhook(@RequestBody String requestBody) throws Exception {
        String result = OkHttpUtils.post(ApiConstants.Webhook.GET_WEBHOOK, requestBody);
        return R.ok(JSONObject.parseObject(result));
    }
}