package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.CSDNBlog;

public interface CSDNService {
    JSONObject getCSDNBlogMessage(String url,int type);
}
