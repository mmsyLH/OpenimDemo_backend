package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.GetTokenReq;
import com.freenet.openimdemo.bean.vo.TokenResponse;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.alibaba.fastjson2.JSON;
import com.freenet.openimdemo.utils.R;
import com.freenet.openimdemo.bean.vo.RegisterReq;
import com.freenet.openimdemo.bean.vo.ServerRegisterReq;
import com.freenet.openimdemo.bean.vo.LoginReq;
import com.freenet.openimdemo.bean.vo.ServerLoginReq;
import com.freenet.openimdemo.bean.vo.AIGroupCreateReq;
import com.freenet.openimdemo.utils.Utils;
import com.freenet.openimdemo.bean.vo.AccountCheckReq;
import com.freenet.openimdemo.bean.vo.AccountCheckResp;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Token相关接口控制器
 */
@RestController
@RequestMapping("/auth")  // 添加auth路径前缀
@CrossOrigin(origins = "http://localhost:3003", allowCredentials = "true")  // 修改这里
public class TokenController {

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

    /**
     * 获取用户Token
     *
     * @param getTokenReq 请求参数，包含platformID和userID
     * @return 用户Token信息
     * @throws Exception 例外
     */
    @PostMapping("/getUserToken")
    public R<TokenResponse> getUserToken(@RequestBody GetTokenReq getTokenReq) throws Exception {
        // 先获取管理员token
        GetTokenReq adminTokenReq = new GetTokenReq();
        adminTokenReq.setUserID("imAdmin");
        adminTokenReq.setSecret("openIM123");
        adminTokenReq.setPlatformID(1);  // 设置平台ID为1
        
        // 发送请求获取管理员token
        String adminJsonBody = JSON.toJSONString(adminTokenReq);
        String adminResult = OkHttpUtils.post(ApiConstants.Auth.GET_ADMIN_TOKEN, adminJsonBody);
        TokenResponse adminTokenResponse = JSONObject.parseObject(adminResult, TokenResponse.class);
        
        // 获取管理员token
        String adminToken = adminTokenResponse.getData().getToken();
        
        // 将用户请求参数转换为JSON字符串
        String userJsonBody = JSON.toJSONString(getTokenReq);
        
        // 构建请求对象，设置必要的请求头
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.Auth.GET_USER_TOKEN)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", adminToken)  // 使用获取到的管理员token
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive")
                .post(okhttp3.RequestBody.create(userJsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送请求并处理响应
        String result = OkHttpUtils.post(request);
        TokenResponse tokenResponse = JSONObject.parseObject(result, TokenResponse.class);
        return R.ok(tokenResponse);
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
     * @throws Exception 例外
     */
    @GetMapping("/login")
    public R<Object> login(String phoneNumber) throws Exception {
        // 1. 检查用户是否已注册
        AccountCheckReq checkReq = new AccountCheckReq();
        List<String> checkUserIDs = new ArrayList<>();
        checkUserIDs.add(phoneNumber); // 使用手机号作为userID
        checkReq.setCheckUserIDs(checkUserIDs);
        
        // 构建检查请求
        String checkJsonBody = JSON.toJSONString(checkReq);
        okhttp3.Request checkRequest = new okhttp3.Request.Builder()
                .url(ApiConstants.User.ACCOUNT_CHECK)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .addHeader("Content-Type", "application/json")
                .post(okhttp3.RequestBody.create(checkJsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送检查请求
        String checkResult = OkHttpUtils.post(checkRequest);
        AccountCheckResp checkResp = JSONObject.parseObject(checkResult, AccountCheckResp.class);
        
        // 2. 如果未注册，先进行注册
        if (checkResp.getErrCode() == 0 && 
            !checkResp.getData().getResults().isEmpty() && 
            checkResp.getData().getResults().get(0).getAccountStatus() == 0) {
            
            // 构建注册请求
            RegisterReq registerReq = new RegisterReq();
            registerReq.setPhoneNumber(phoneNumber);
            registerReq.setNickname("用户" + phoneNumber); // 使用默认昵称
            
            // 发送注册请求
            register(registerReq);
        }
        
        // 3. 进行登录
        ServerLoginReq serverReq = new ServerLoginReq();
        serverReq.setPhoneNumber(phoneNumber);
        
        // 构建登录请求
        String loginJsonBody = JSON.toJSONString(serverReq);
        okhttp3.Request loginRequest = new okhttp3.Request.Builder()
                .url(ApiConstants.Account.LOGIN)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/json")
                .addHeader("Origin", "http://localhost:3003")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Mobile Safari/537.36")
                .post(okhttp3.RequestBody.create(loginJsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送登录请求并获取响应
        String loginResult = OkHttpUtils.post(loginRequest);
        JSONObject loginResponse = JSONObject.parseObject(loginResult);
        
        // 4. 如果登录成功，创建AI群聊
        if (loginResponse.getInteger("errCode") == 0) {
            String userID = loginResponse.getJSONObject("data").getString("userID");
            
            // 构建创建群聊请求
            AIGroupCreateReq groupReq = new AIGroupCreateReq();
            
            // 设置群成员（用户ID和固定的客服ID）
            List<String> memberIds = new ArrayList<>();
            memberIds.add(userID);
            groupReq.setMemberUserIDs(memberIds);
            groupReq.setOwnerUserID("10086");
            
            // 设置群信息
            AIGroupCreateReq.GroupInfo groupInfo = new AIGroupCreateReq.GroupInfo();
            groupInfo.setGroupID(String.valueOf(System.currentTimeMillis())); // 使用时间戳作为群ID
            groupReq.setGroupInfo(groupInfo);
            
            // 构建创建群聊的请求
            String groupJsonBody = JSON.toJSONString(groupReq);
            okhttp3.Request groupRequest = new okhttp3.Request.Builder()
                    .url(ApiConstants.Group.CREATE_GROUP)
                    .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                    .addHeader("token", Utils.getAdminToken()) // 使用管理员token创建群组
                    .addHeader("Content-Type", "application/json")
                    .post(okhttp3.RequestBody.create(groupJsonBody, OkHttpUtils.JSON))
                    .build();
            
            // 发送创建群聊请求
            String groupResult = OkHttpUtils.post(groupRequest);
            JSONObject groupResponse = JSONObject.parseObject(groupResult);
            
            // 将群组信息添加到登录响应中
            loginResponse.put("groupInfo", groupResponse);
        }
        
        return R.ok(loginResponse);
    }
}
