package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.CSDNBlog;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface CSDNService {
    JSONObject getCSDNBlogMessage(String url,int type) throws NoSuchAlgorithmException, KeyManagementException;
}
