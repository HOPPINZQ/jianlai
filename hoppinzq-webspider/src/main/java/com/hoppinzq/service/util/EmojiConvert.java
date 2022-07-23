package com.hoppinzq.service.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: zq
 */
public class EmojiConvert {

    /**
     * @Description emoji表情转换
     * @param str 待转换字符串
     * @return 转换后字符串
     * @throws UnsupportedEncodingException
     */
    public static String emojiConvertToUtf(String str)
            throws UnsupportedEncodingException {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(
                        sb,
                        "[[" + URLEncoder.encode(matcher.group(1),
                                "UTF-8") + "]]");
            } catch (UnsupportedEncodingException e) {
                throw e;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * @Description 还原emoji表情的字符串
     *
     * @param str 转换后的字符串
     * @return 转换前的字符串
     * @throws UnsupportedEncodingException
     */
    public static String utfemojiRecovery(String str)
            throws UnsupportedEncodingException {
        String patternString = "\\[\\[(.*?)\\]\\]";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb,
                        URLDecoder.decode(matcher.group(1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw e;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
