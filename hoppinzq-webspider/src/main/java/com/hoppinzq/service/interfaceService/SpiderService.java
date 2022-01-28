package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONArray;
import com.hoppinzq.service.bean.SpiderLink;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

public interface SpiderService {
    void startWork(String url);
    JSONArray queryweb(String search);
}
