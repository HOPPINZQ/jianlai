package com.hoppinzq.hoppinzqcode.util;

import java.util.UUID;

/**
 * @author:ZhangQi
 **/
public class UUIDUtil {
    private UUIDUtil(){}

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
