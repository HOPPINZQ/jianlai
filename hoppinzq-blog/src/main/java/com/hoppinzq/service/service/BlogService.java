package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceLimit;
import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.bean.FormInfo;
import com.hoppinzq.service.dao.BlogDao;
import com.hoppinzq.service.util.Base64Util;
import com.hoppinzq.service.util.FileUtil;
import com.hoppinzq.service.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApiServiceMapping(title = "博客服务", description = "博客服务，已加入网关")
public class BlogService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "getBlogClass", title = "获取博客类别", description = "获取的是类别树，从redis里获取，找不到则兜底从数据库获取并存入redis")
    public JSONObject getBlogClass(Long userId) {
        System.err.println(stringRedisTemplate.opsForValue().get("name"));

        redisUtils.set("name1","zhangqi1");
        System.err.println(redisUtils.get("name1"));

        List<Map> Map=blogDao.queryBlogClass();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",userId);
        jsonObject.put("age",6);
        return jsonObject;
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogVideo", title = "博客测试", description = "博客测试")
    public JSONArray blogVideo(List<LinkedHashMap> formInfos,String blogType) throws IOException, ClassNotFoundException{
        ObjectMapper mapper=new ObjectMapper();
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<formInfos.size();i++){
            JSONObject jsonObject=new JSONObject();
            FormInfo formInfo = mapper.convertValue(formInfos.get(i), FormInfo.class);
            jsonObject.put("name",formInfo.getInputStream());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("id",blogType);
        jsonArray.add(jsonObject1);
        return jsonArray;
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogImgUpload", title = "博客图片上传", description = "博客图片上传")
    public JSONObject blogImgUpload(List<LinkedHashMap> formInfos,String blogType) throws IOException, ClassNotFoundException{
        ObjectMapper mapper=new ObjectMapper();
        String fileName="";
        for(int i=0;i<formInfos.size();i++){
            FormInfo formInfo = mapper.convertValue(formInfos.get(i), FormInfo.class);
            InputStream inputStream= Base64Util.baseToInputStream(formInfo.getInputStream());
            fileName=formInfo.getSubmittedFileName();
            FileUtil.saveFile(inputStream,fileName,"D:\\projectFile\\markdown");
        }
        JSONObject jsonObject=new JSONObject();
        if("markdown".equals(blogType)){
            jsonObject.put("success",1);
            jsonObject.put("message","上传成功");
            jsonObject.put("url","http://127.0.0.1:8809/markdown/"+fileName);
        }else if("fwb".equals(blogType)){
            JSONArray jsonArray=new JSONArray();
            jsonArray.add("http://127.0.0.1:8809/markdown/"+fileName);
            jsonObject.put("errno",0);
            jsonObject.put("data",jsonArray);
        }
        return jsonObject;
    }

}
