package com.hoppinzq.service.util;

import java.util.Arrays;
import java.util.List;

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

    public static String notNull(String str){
        return str==null?"":str;
    }

    public static List getStaticList(String[] strings){
        return Arrays.asList(strings);
    }


}
