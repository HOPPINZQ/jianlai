package com.hoppinzq.service.interfaceService;

import java.io.InputStream;
import java.util.Map;

public interface ChatService {

    Map getChatMessage(String msg);
    Map getRtVoice(InputStream inputStream);
}
