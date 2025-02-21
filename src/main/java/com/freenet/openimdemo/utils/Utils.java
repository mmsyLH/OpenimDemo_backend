package com.freenet.openimdemo.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.freenet.openimdemo.bean.vo.GetTokenReq;
import com.freenet.openimdemo.bean.vo.TokenResponse;
import com.freenet.openimdemo.constants.ApiConstants;

public class Utils {

    /**
     * 获取管理员token
     *
     * @return 管理员token
     * @throws Exception 获取token异常
     */
    public static String getAdminToken() throws Exception {
        // 构建获取管理员token的请求
        GetTokenReq adminTokenReq = new GetTokenReq();
        adminTokenReq.setUserID("imAdmin");
        adminTokenReq.setSecret("openIM123");

        // 发送请求获取管理员token
        String adminJsonBody = JSON.toJSONString(adminTokenReq);
        String adminResult = OkHttpUtils.post(ApiConstants.Auth.GET_ADMIN_TOKEN, adminJsonBody);
        TokenResponse adminTokenResponse = JSONObject.parseObject(adminResult, TokenResponse.class);

        return adminTokenResponse.getData().getToken();
    }

}
