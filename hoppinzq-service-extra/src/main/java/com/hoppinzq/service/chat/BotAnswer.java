package com.hoppinzq.service.chat;

import com.hoppinzq.service.chat.asrdemo.AsrMain;
import com.hoppinzq.service.util.SpringUtils;
import com.hoppinzq.service.utils.HttpClientComm;

import java.io.InputStream;

/**
 * @author:ZhangQi
 **/
public class BotAnswer {
    private final static String key="9688cd051e804ae16a001bd5406d60ec";

    public static String answer(String msg){
        HttpClientComm httpClientComm=SpringUtils.getBean(HttpClientComm.class);
        return httpClientComm.get("http://api.tianapi.com/txapi/robot/index?key="+key+"&question="+msg);
    }

    public static String rtVoice(InputStream inputStream){
        AsrMain asrMain=SpringUtils.getBean(AsrMain.class);
        return asrMain.getSbibieFile(inputStream);
    }
}
