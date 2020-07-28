package com.dataway.cn.utils;

import java.util.Arrays;

/**
 * @author phil
 * @date 2020/05/19 16:25
 */
public class CheckUtil {
    private static final String TOKEN = "deea5271d11a40fa9f19c1475ed4a087";
    public static boolean checkSignature(String signature,String timestamp,String nonce){
        String[] str = new String[]{TOKEN,timestamp,nonce};
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuilder buffer = new StringBuilder();
        for (String s : str) {
            buffer.append(s);
        }
        //进行sha1加密
        String temp = SHA1.encode(buffer.toString());
        //与微信提供的signature进行匹对
        return signature.equals(temp);
    }
}
