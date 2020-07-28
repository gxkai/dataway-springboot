package com.dataway.cn.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * Http 工具
 * @author phil
 * @date 2020/06/18 14:31
 */
public class HttpUtil {

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 JSONObject 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, JSONObject param) {
        MediaType mediaType=MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody requestBody=RequestBody.Companion.create(param.toJSONString(),mediaType);
        //创建request 示例
        //这里也可以 .header(key,value)
        Request request = new Request
                .Builder()
                .url(url)
                .post(requestBody)
                .build();
        return sendRequest(request);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     * 参数拼在url后面
     * @param url   发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        //创建request 示例
        //这里也可以 .header(key,value)
        Request request = new Request
                .Builder()
                .url(url)
                .get()
                .build();
        return sendRequest(request);
    }

    public static String sendFile(String url,File file){
        //File转RequestBody
        RequestBody fileBody = RequestBody.create(file,MediaType.parse("application/octet-stream"));
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("media",file.getName(),fileBody)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url).build();
        return sendRequest(request);
    }
    /**
     * 向指定 URL 发送Request方法的请求
     * @return 所代表远程资源的响应结果
     */
    private static String sendRequest(Request request) {
        String result;
        //创建客户端
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                //注：这里的 response.body().string() 只能取一次，取多次会抛出异常
                result = response.body().string();
            } else {
                result = "{\"code\": \"400\",\"message\": \"系统错误\"}";
            }
        } catch (IOException e) {
            result = "{\"code\": \"400\",\"message\": \"系统错误\"}";
            e.printStackTrace();
        }
        JSONObject jsonObject=JSONObject.parseObject(result);
        return jsonObject.toJSONString();
    }
}
