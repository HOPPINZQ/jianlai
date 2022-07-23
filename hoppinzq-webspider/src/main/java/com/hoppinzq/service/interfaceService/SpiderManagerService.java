package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.SpiderMajor;

import java.util.List;
import java.util.Map;

public interface SpiderManagerService {
    JSONObject insertSpiders(SpiderMajor spiderMajor, List<Map> spiderBeans);
    JSONObject getSpiderMessage(Long id ,String url);
    SpiderMajor querySpiderById(Long id);
}
