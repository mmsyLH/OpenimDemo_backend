package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.CreateGroupReq;
import com.freenet.openimdemo.bean.vo.CreateGroupResp;
import com.freenet.openimdemo.bean.vo.InviteGroupReq;
import com.freenet.openimdemo.bean.vo.GetJoinedGroupReq;
import com.freenet.openimdemo.bean.vo.GetJoinedGroupResp;
import com.freenet.openimdemo.config.AIServiceConfig;
import com.freenet.openimdemo.config.HumanServiceConfig;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.R;
import com.freenet.openimdemo.utils.Utils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 群组管理控制器
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    private final AIServiceConfig aiConfig;
    private final HumanServiceConfig humanConfig;
    
    public GroupController(AIServiceConfig aiConfig, HumanServiceConfig humanConfig) {
        this.aiConfig = aiConfig;
        this.humanConfig = humanConfig;
    }

    /**
     * 创建群组
     *
     * @param createGroupReq 创建群组请求参数
     * @return 创建结果
     */
    @PostMapping("/createGroup")
    public R<CreateGroupResp> createGroup(@RequestBody CreateGroupReq createGroupReq) throws Exception {
        // 构建请求
        String jsonBody = JSON.toJSONString(createGroupReq);
        
        // 构建请求对象，设置必要的请求头
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Group.CREATE_GROUP)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive")
                .post(okhttp3.RequestBody.create(jsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送请求并处理响应
        String result = OkHttpUtils.post(request);
        CreateGroupResp resp = JSONObject.parseObject(result, CreateGroupResp.class);
        return R.ok(resp);
    }

    /**
     * 获取群组信息
     */
    @PostMapping("/getGroupsInfo")
    public R<Object> getGroupsInfo(@RequestBody String requestBody) throws Exception {
        String result = OkHttpUtils.post(ApiConstants.Group.GET_GROUPS_INFO, requestBody);
        return R.ok(JSONObject.parseObject(result));
    }

    /**
     * 邀请用户加入群组
     *
     * @param inviteReq 邀请请求参数
     * @return 邀请结果
     */
    @PostMapping("/inviteUserToGroup")
    public R<Object> inviteUserToGroup(@RequestBody InviteGroupReq inviteReq) throws Exception {
        // 构建请求
        String jsonBody = JSON.toJSONString(inviteReq);
        
        // 构建请求对象，设置必要的请求头
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Group.INVITE_USER_TO_GROUP)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive")
                .post(okhttp3.RequestBody.create(jsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送请求并处理响应
        String result = OkHttpUtils.post(request);
        return R.ok(JSONObject.parseObject(result));
    }

    /**
     * 获取用户已加入的群组列表
     *
     * @param req 请求参数
     * @return 群组列表
     */
    @PostMapping("/getJoinedGroupList")
    public R<GetJoinedGroupResp> getJoinedGroupList(@RequestBody GetJoinedGroupReq req) throws Exception {
        // 构建请求
        String jsonBody = JSON.toJSONString(req);
        
        // 构建请求对象，设置必要的请求头
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Group.GET_JOINED_GROUP_LIST)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive")
                .post(okhttp3.RequestBody.create(jsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送请求并处理响应
        String result = OkHttpUtils.post(request);
        GetJoinedGroupResp resp = JSONObject.parseObject(result, GetJoinedGroupResp.class);
        return R.ok(resp);
    }

    /**
     * 转人工客服
     *
     * @param groupID 群组ID
     * @return 转人工结果
     */
    @GetMapping("/transferToHuman")
    public R<Object> transferToHuman(@RequestParam String groupID) {
        try {
            // 1. 构建邀请请求
            JSONObject req = new JSONObject();
            req.put("groupID", groupID);
            req.put("invitedUserIDs", Collections.singletonList(humanConfig.getUserId()));
            req.put("reason", "用户请求转人工服务");
            
            // 2. 发送邀请请求
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(ApiConstants.Group.INVITE_USER_TO_GROUP)
                    .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                    .addHeader("token", Utils.getAdminToken())
                    .post(okhttp3.RequestBody.create(req.toJSONString(), OkHttpUtils.JSON))
                    .build();
            
            String result = OkHttpUtils.post(request);
            JSONObject resp = JSONObject.parseObject(result);
            
            // 3. 如果邀请成功，发送提示消息
            if (resp.getInteger("errCode") == 0) {
                sendAINotification(groupID, "人工客服已接入，请稍候...");
                return R.ok(resp);
            } else {
                return R.fail(resp.getString("errMsg"));
            }
        } catch (Exception e) {
            return R.fail("转人工失败: " + e.getMessage());
        }
    }

    /**
     * 发送AI通知消息
     */
    private void sendAINotification(String groupID, String content) throws Exception {
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
} 