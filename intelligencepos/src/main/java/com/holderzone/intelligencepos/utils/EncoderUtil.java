package com.holderzone.intelligencepos.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dengliwen on 2017-5-26.
 * MD5加密工具类
 */
public class EncoderUtil {
    private EncoderUtil() {
    }

    /**
     * MD5加密字符串
     *
     * @param str 明文密码
     * @return 加密后的密码
     */
    public static String encode(String str) {
        if (android.text.TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] byteArray = str.getBytes();
            md.update(byteArray);
            byte[] result = md.digest();
            return byteArrayToHex(result);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * byte[]数组转字符串
     *
     * @param byteArray byte[]
     * @return String
     */
    private static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }
}