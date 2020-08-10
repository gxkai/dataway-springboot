package com.dataway.cn.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 安全Util 包含一些加減密的算法
 * @author phil
 * @date 2020/08/10 14:00
 */
public class SecurityUtil {

    /**
     * Md5加密(可以加密中文)
     * @param plainText:需要加密的字符串
     * @return String(返回加密后的字符串)
     */
    public static String stringToMd5(String plainText) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("MD5 should be supported?", var7);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var6 = hash;
        int var5 = hash.length;
        for(int var4 = 0; var4 < var5; ++var4) {
            byte b = var6[var4];
            if((b & 255) < 16) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 255));
        }
        return hex.toString();
    }
}
