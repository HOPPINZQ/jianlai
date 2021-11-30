package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceLimit;
import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.bean.FormInfo;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

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
    public JSONArray blogVideo(List<LinkedHashMap> formInfos,int userId) throws IOException, ClassNotFoundException{
        ObjectMapper mapper=new ObjectMapper();
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<formInfos.size();i++){
            JSONObject jsonObject=new JSONObject();
            FormInfo formInfo = mapper.convertValue(formInfos.get(i), FormInfo.class);
            jsonObject.put("name",formInfo.getInputStream());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("id",userId);
        jsonArray.add(jsonObject1);
        return jsonArray;
    }



}
