package com.hoppinzq.service.util;

/**
 * @author: zq
 */
public class StringUtils {

    private StringUtils(){}

    /**
     * 字符串为null或者空返回true
     * @param str
     * @return
     */
    public static Boolean isBlank(String str){
        if(str==null)
            return true;
        if(str.trim().length()==0)
            return true;
        return false;
    }
}
