package com.hoppinzq.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.chat.BotAnswer;
import com.hoppinzq.service.interfaceService.ChatService;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * @author:ZhangQi
 **/
@ServiceRegister
public class ChatServiceImpl implements ChatService, Serializable {

    private static final long serialVersionUID = 2783377098145240357L;

    @Override
    public Map getChatMessage(String msg) {
        Map resultMap = JSONObject.parseObject(new BotAnswer().answer(msg), Map.class);
        return resultMap;
    }

    @Override
    public Map getRtVoice(InputStream inputStream) {
        Map resultMap = JSONObject.parseObject(new BotAnswer().rtVoice(inputStream), Map.class);
        return resultMap;
    }
}
