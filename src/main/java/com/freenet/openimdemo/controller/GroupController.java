package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.CreateGroupReq;
import com.freenet.openimdemo.bean.vo.CreateGroupResp;
import com.freenet.openimdemo.bean.vo.InviteGroupReq;
import com.freenet.openimdemo.bean.vo.GetJoinedGroupReq;
import com.freenet.openimdemo.bean.vo.GetJoinedGroupResp;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.R;
import com.freenet.openimdemo.utils.Utils;
import org.springframework.web.bind.annotation.*;

/**
 * 群组管理控制器
 */
@RestController
@RequestMapping("/group")
public class GroupController {

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
} 