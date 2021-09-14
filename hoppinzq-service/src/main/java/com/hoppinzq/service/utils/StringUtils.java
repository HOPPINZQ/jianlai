package com.hoppinzq.service.utils;

/**
 * @author:ZhangQi
 **/
public class StringUtils {

    private StringUtils(){}

    public static String join(Object[] objectss){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<objectss.length;i++){
            sb.append(objectss[i].toString());
            if(i!=objectss.length-1){
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
