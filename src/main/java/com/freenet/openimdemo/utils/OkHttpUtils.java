package com.freenet.openimdemo.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {
    private static final Logger log = LoggerFactory.getLogger(OkHttpUtils.class);
    
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @return 响应字符串
     */
    public static String get(String url) throws IOException {
        return get(url, null, null);
    }

    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @param params 请求参数
     * @return 响应字符串
     */
    public static String get(String url, Map<String, String> params) throws IOException {
        return get(url, params, null);
    }

    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应字符串
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        // 构建URL带参数
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        // 构建请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive");

        // 添加自定义请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 执行请求
        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String result = response.body().string();
            log.info("GET Response: {}", result);
            return result;
        }
    }

    /**
     * 发送POST请求
     *
     * @param url 请求URL
     * @param json JSON格式的请求体
     * @return 响应字符串
     */
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("operationID",String.valueOf(System.currentTimeMillis()) )
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String result = response.body().string();
            log.info("POST Response: {}", result);
            return result;
        }
    }

    /**
     * 发送POST请求
     *
     * @param request 自定义请求对象
     * @return 响应字符串
     */
    public static String post(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String result = response.body().string();
            log.info("POST Response: {}", result);
            return result;
        }
    }
} 