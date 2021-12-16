package com.hoppinzq.service.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * base64及其他编解码工具类
 */
public class Base64Util {

    /**
     * base64转inputStream
     * @param base64string
     * @return
     */
    public static InputStream baseToInputStream(String base64string){
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }

    /**
     * base64转inputStream
     * @param base64byte
     * @return
     */
    public static InputStream baseToInputStream(final byte[] base64byte){
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes1 = Base64.decodeBase64(base64byte);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }

    public static String inputStreamToBase(InputStream inputStream) throws IOException {

        byte[] bytes = IOUtils.toByteArray(inputStream);
        String encoded = java.util.Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }

    /**
     * 将字符串中的中文进行编码
     * @param str
     * @return 返回字符串中汉字编码后的字符串
     */
    public static String cnToEncode(String str){
        char[] ch = str.toCharArray();
        String result = "";
        for(int i=0;i<ch.length;i++){
            char temp = ch[i];
            if(isChinese(temp)){
                try {
                    String encode = URLEncoder.encode(String.valueOf(temp), "utf-8");
                    result = result + encode;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                result = result+temp;
            }
        }
        return result;
    }
    /**
     * 判断字符是否为汉字
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * base64编码
     * @param str
     * @return
     */
    public static String base64Encode(String str) {
        byte[] encodedBytes = java.util.Base64.getEncoder().encode(str.getBytes());
        return new String(encodedBytes,java.nio.charset.Charset.forName("UTF-8"));
    }

    /**
     * base64解码
     * @param str
     * @return
     */
    public static String base64Decode(String str){
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(str.getBytes());
        return new String(decodedBytes, java.nio.charset.Charset.forName("UTF-8"));
    }

    /**
     * base64解码plus
     * 将空格解析为+
     * @param str
     * @return
     */
    public static String base64DecodePlus(String str){
        String _str=str.trim().replaceAll(" ","+");
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(_str.getBytes());
        return new String(decodedBytes, java.nio.charset.Charset.forName("UTF-8"));
    }

    /**
     * 解码
     * @param str
     * @return
     */
    public static String encodeDecode(String str){
        return java.net.URLDecoder.decode(str);
    }

}
