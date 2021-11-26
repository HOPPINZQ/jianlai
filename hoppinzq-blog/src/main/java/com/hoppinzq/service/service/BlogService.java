package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceLimit;

@ApiServiceMapping(title = "博客服务", description = "博客服务，已加入网关")
public class BlogService {

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogTest", title = "博客测试", description = "博客测试")
    public JSONObject getUser(Long userId) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name","zhangqi");
        jsonObject.put("age",6);
        return jsonObject;
    }

}
