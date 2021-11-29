package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceLimit;
import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.bean.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

@ApiServiceMapping(title = "博客服务", description = "博客服务，已加入网关")
public class BlogService {

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogTest", title = "博客测试", description = "博客测试")
    public JSONObject getUser(Blog blog) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",blog.getId());
        jsonObject.put("age",6);
        return jsonObject;
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogVideo", title = "博客测试", description = "博客测试")
    public JSONObject blogVideo(FileInfo fileInfo) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",fileInfo.getName());

        return jsonObject;
    }



}
