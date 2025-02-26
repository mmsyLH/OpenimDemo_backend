package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.*;
import com.freenet.openimdemo.config.AIServiceConfig;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.alibaba.fastjson2.JSON;
import com.freenet.openimdemo.utils.R;
import com.freenet.openimdemo.utils.Utils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Token相关接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")  // 添加auth路径前缀
@CrossOrigin(origins = "http://localhost:3003", allowCredentials = "true")  // 修改这里
public class TokenController {

    private final AIServiceConfig aiConfig;
    
    public TokenController(AIServiceConfig aiConfig) {
        this.aiConfig = aiConfig;
    }

    /**
     * 获取管理员Token
     * 
     * @param getTokenReq 请求参数，包含platformID和userID
     * @return 管理员Token信息
     */
    @PostMapping("/getAdminToken")
    public R<TokenResponse> getAdminToken(@RequestBody GetTokenReq getTokenReq) throws Exception {
        // 将请求体转换为JSON字符串
        String jsonBody = JSON.toJSONString(getTokenReq);
        
        // 发送POST请求
        String result = OkHttpUtils.post(ApiConstants.Auth.GET_ADMIN_TOKEN, jsonBody);
        TokenResponse tokenResponse = JSONObject.parseObject(result, TokenResponse.class);
        return R.ok(tokenResponse);
    }

//    /**
//     * 获取用户Token
//     *
//     * @param getTokenReq 请求参数，包含platformID和userID
//     * @return 用户Token信息
//     * @throws Exception 例外
//     */
//    @PostMapping("/getUserToken")
//    public R<TokenResponse> getUserToken(@RequestBody GetTokenReq getTokenReq) {
//        try {
//            // 1. 调用获取token接口
//            String result = OkHttpUtils.post(ApiConstants.Auth.GET_USER_TOKEN, JSON.toJSONString(getTokenReq));
//            TokenResponse tokenResp = JSONObject.parseObject(result, TokenResponse.class);
//
//            if (tokenResp.getErrCode() == 0) {
//                // 2. 检查是否已有AI客服群聊
//                String aiGroupID = findOrCreateAIServiceGroup(getTokenReq.getUserID(), tokenResp.getToken());
//                tokenResp.setAiGroupID(aiGroupID);
//            }
//
//            return R.ok(tokenResp);
//        } catch (Exception e) {
//            log.error("获取用户token失败", e);
//            return R.fail("获取用户token失败: " + e.getMessage());
//        }
//    }

    /**
     * 查找或创建AI客服群聊
     */
    private String findOrCreateAIServiceGroup(String userID, String token) throws Exception {
        // 1. 获取已加入的群组列表
        JSONObject req = new JSONObject();
        req.put("fromUserID", userID);
        req.put("pagination", new JSONObject()
                .fluentPut("pageNumber", 1)
                .fluentPut("showNumber", 100));
        
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Group.GET_JOINED_GROUP_LIST)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", token)
                .post(okhttp3.RequestBody.create(req.toJSONString(), OkHttpUtils.JSON))
                .build();
        
        String result = OkHttpUtils.post(request);
        JSONObject resp = JSONObject.parseObject(result);
        
        // 2. 查找AI客服群聊
        if (resp.getInteger("errCode") == 0) {
            JSONObject data = resp.getJSONObject("data");
            JSONArray groups = data.getJSONArray("groups");
            for (int i = 0; i < groups.size(); i++) {
                JSONObject group = groups.getJSONObject(i);
                if (aiConfig.getUserId().equals(group.getString("ownerUserID"))) {
                    return group.getString("groupID");
                }
            }
        }
        
        // 3. 如果没有找到，创建新的AI客服群聊
        return createAIServiceGroup(userID);
    }

    /**
     * 创建AI客服群聊
     */
    private String createAIServiceGroup(String userID) throws Exception {
        // 1. 创建群组
        CreateGroupReq createGroupReq = new CreateGroupReq();
        createGroupReq.setOwnerUserID(aiConfig.getUserId());
        createGroupReq.setMemberUserIDs(Collections.singletonList(userID));
        
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Group.CREATE_GROUP)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .post(okhttp3.RequestBody.create(JSON.toJSONString(createGroupReq), OkHttpUtils.JSON))
                .build();
        
        String result = OkHttpUtils.post(request);
        JSONObject resp = JSONObject.parseObject(result);
        
        // 2. 发送欢迎消息
        if (resp.getInteger("errCode") == 0) {
            String groupID = resp.getJSONObject("data").getString("groupID");
            sendAIWelcomeMessage(groupID);
            return groupID;
        }
        
        throw new RuntimeException("创建群聊失败: " + resp.getString("errMsg"));
    }

    /**
     * 发送AI欢迎消息
     */
    private void sendAIWelcomeMessage(String groupID) throws Exception {
        JSONObject req = new JSONObject();
        req.put("sendID", aiConfig.getUserId());
        req.put("groupID", groupID);
        req.put("senderNickname", aiConfig.getGroup().getName());
        req.put("content", new JSONObject().fluentPut("content", "群聊已创建，我是智能客服，请问有什么可以帮您？"));
        req.put("contentType", 101);
        req.put("sessionType", 3);
        
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Message.SEND_MSG)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .post(okhttp3.RequestBody.create(req.toJSONString(), OkHttpUtils.JSON))
                .build();
        
        OkHttpUtils.post(request);
    }

    /**
     * 用户注册
     *
     * @param registerReq 注册请求参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public R<Object> register(@RequestBody RegisterReq registerReq) throws Exception {
        // 构建服务端注册请求
        ServerRegisterReq serverReq = new ServerRegisterReq();
        
        // 设置用户信息
        ServerRegisterReq.UserInfo userInfo = new ServerRegisterReq.UserInfo();
        userInfo.setNickname(registerReq.getNickname());
        userInfo.setPhoneNumber(registerReq.getPhoneNumber());
        serverReq.setUser(userInfo);
        
        // 构建请求
        String jsonBody = JSON.toJSONString(serverReq);
        
        // 构建请求对象，设置必要的请求头
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Account.REGISTER)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/json")
                .addHeader("Origin", "http://localhost:3003")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Mobile Safari/537.36")
                .post(okhttp3.RequestBody.create(jsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送请求并处理响应
        String result = OkHttpUtils.post(request);
        return R.ok(JSONObject.parseObject(result));
    }

    /**
     * 用户登录
     *
     * @param phoneNumber 电话号码
     * @return 登录结果
     */
    @GetMapping("/login")
    public R<Object> login(String phoneNumber) throws Exception {
        // 1. 构建登录请求
        ServerLoginReq loginReq = new ServerLoginReq();
        loginReq.setPhoneNumber(phoneNumber);
        
        // 2. 发送登录请求
        String loginResult = OkHttpUtils.post(ApiConstants.Account.LOGIN, JSON.toJSONString(loginReq));
        JSONObject loginResp = JSONObject.parseObject(loginResult);

        String adminToken = Utils.getAdminToken();
        // 3. 如果登录失败且是未注册错误，先注册用户
            if (loginResp.getInteger("errCode") == 20002) { // 假设20002是未注册错误码
                // 构建注册请求
                ServerRegisterReq serverReq = new ServerRegisterReq();
                ServerRegisterReq.UserInfo userInfo = new ServerRegisterReq.UserInfo();
                userInfo.setNickname("用户" + phoneNumber);
                userInfo.setPhoneNumber(phoneNumber);
                serverReq.setUser(userInfo);
                
                // 发送注册请求
                String registerResult = OkHttpUtils.post(ApiConstants.Account.REGISTER, JSON.toJSONString(serverReq));
                JSONObject registerResp = JSONObject.parseObject(registerResult);
                
                if (registerResp.getInteger("errCode") != 0) {
                    return R.fail("注册失败: " + registerResp.getString("errMsg"));
                }
                
                // 注册成功后重新登录
                loginResult = OkHttpUtils.post(ApiConstants.Account.LOGIN, JSON.toJSONString(loginReq));
                loginResp = JSONObject.parseObject(loginResult);
            }
            
            // 4. 获取用户ID
            String userID = loginResp.getJSONObject("data").getString("userID");
            
            // 5. 获取已加入的群组列表
            JSONObject req = new JSONObject();
            req.put("fromUserID", userID);
            req.put("pagination", new JSONObject()
                    .fluentPut("pageNumber", 1)
                    .fluentPut("showNumber", 100));
            
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(ApiConstants.Group.GET_JOINED_GROUP_LIST)
                    .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                    .addHeader("token",adminToken)
                    .post(okhttp3.RequestBody.create(req.toJSONString(), OkHttpUtils.JSON))
                    .build();
            
            String result = OkHttpUtils.post(request);
            JSONObject resp = JSONObject.parseObject(result);
            
            String aiGroupID = null;
            // 6. 查找AI客服群聊
            if (resp.getInteger("errCode") == 0) {
                JSONObject data = resp.getJSONObject("data");
                if (data != null && data.getJSONArray("groups") != null) {
                    JSONArray groups = data.getJSONArray("groups");
                    for (int i = 0; i < groups.size(); i++) {
                        JSONObject group = groups.getJSONObject(i);
                        if (aiConfig.getUserId().equals(group.getString("ownerUserID"))) {
                            aiGroupID = group.getString("groupID");
                            // 找到AI群聊后，将群组信息添加到登录响应中
                            loginResp.put("groupInfo", group);
                            break;
                        }
                    }
                }
            }
            
            // 7. 如果没有找到AI客服群聊，创建新的
            if (aiGroupID == null) {
                CreateGroupReq createGroupReq = new CreateGroupReq();
                createGroupReq.setOwnerUserID(aiConfig.getUserId());
                createGroupReq.setMemberUserIDs(Collections.singletonList(userID));
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupName(aiConfig.getGroup().getName());
                //不用设置群公告和群介绍
//                groupInfo.setNotification(aiConfig.getGroup().getNotification());
//                groupInfo.setIntroduction(aiConfig.getGroup().getIntroduction());
                groupInfo.setFaceURL(aiConfig.getGroup().getFaceUrl());
                createGroupReq.setGroupInfo(groupInfo);
                
                request = new okhttp3.Request.Builder()
                        .url(ApiConstants.Group.CREATE_GROUP)
                        .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                        .addHeader("token", adminToken)
                        .post(okhttp3.RequestBody.create(JSON.toJSONString(createGroupReq), OkHttpUtils.JSON))
                        .build();
                
                result = OkHttpUtils.post(request);
                CreateGroupResp createGroupResp= JSONObject.parseObject(result, CreateGroupResp.class);

                if (createGroupResp!= null && createGroupResp.getErrCode() == 0) {
                    CreateGroupResp.GroupRespData data = createGroupResp.getData();
                    if (data!= null) {
                            groupInfo = data.getGroupInfo();
                        if (groupInfo!= null) {
                            aiGroupID = groupInfo.getGroupID();
                            // 发送欢迎消息
                            sendAIWelcomeMessage(aiGroupID);
                            loginResp.put("groupInfo", groupInfo);
                        }
                    }
                }
            }
        return R.ok(loginResp);
    }
}
