package com.freenet.openimdemo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.GetTokenReq;
import com.freenet.openimdemo.bean.vo.ImportUserReq;
import com.freenet.openimdemo.bean.vo.TokenResponse;
import com.freenet.openimdemo.bean.vo.GetAllUserIDsReq;
import com.freenet.openimdemo.bean.vo.GetAllUserIDsResp;
import com.freenet.openimdemo.constants.ApiConstants;
import com.freenet.openimdemo.utils.OkHttpUtils;
import com.freenet.openimdemo.utils.R;
import com.freenet.openimdemo.utils.Utils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 导入（注册）用户
     *
     * @param importUserReq 导入用户请求参数
     * @return 导入结果
     */
    @PostMapping("/importUser")
    public R<Object> importUser(@RequestBody ImportUserReq importUserReq) throws Exception {
        // 获取管理员token（正式环境使用）
        String adminToken = Utils.getAdminToken();
        
        // 也可以使用测试token：Utils.getAdminToken()
        
        // 构建导入用户请求
        String jsonBody = JSON.toJSONString(importUserReq);
        
        // 构建请求对象，设置必要的请求头
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.User.USER_REGISTER)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", adminToken)  // 使用获取到的管理员token
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
     * 获取已注册用户ID列表
     *
     * @param req 请求参数，包含分页信息
     * @return 用户ID列表
     */
    @PostMapping("/getAllUsersUID")
    public R<GetAllUserIDsResp> getAllUsersUID(@RequestBody GetAllUserIDsReq req) throws Exception {
        // 构建请求
        String jsonBody = JSON.toJSONString(req);
        
        // 构建请求对象，设置必要的请求头
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiConstants.User.GET_ALL_USERS_UID)
                .addHeader("operationID", String.valueOf(System.currentTimeMillis()))
                .addHeader("token", Utils.getAdminToken())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive")
                .post(okhttp3.RequestBody.create(jsonBody, OkHttpUtils.JSON))
                .build();
        
        // 发送请求并处理响应
        String result = OkHttpUtils.post(request);
        GetAllUserIDsResp resp = JSONObject.parseObject(result, GetAllUserIDsResp.class);
        return R.ok(resp);
    }
} 