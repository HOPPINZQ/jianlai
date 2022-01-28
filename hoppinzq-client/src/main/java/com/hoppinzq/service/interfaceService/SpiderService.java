package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONArray;

public interface SpiderService {
    void startWork(String url);
    JSONArray queryweb(String search);
}
