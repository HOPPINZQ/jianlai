package com.hoppinzq.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 计算日期差的工具类
 * @author:ZhangQi
 **/
public class CheckDateUtil {

    private CheckDateUtil(){}

    public static int checkDateCha(String startDate){
        SimpleDateFormat formatter =   new SimpleDateFormat( "yyyy-MM-dd");
        Date date1=new Date();
        Date date = null;
        Long cha = 0L;
        try {
            date = formatter.parse(startDate);
            long ts = date.getTime();
            long ts1 = date1.getTime();
            cha = (ts1 - ts) / (1000 * 60 * 60 * 24);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return cha.intValue();
    }
}
