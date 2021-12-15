package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceLimit;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.bean.FormInfo;
import com.hoppinzq.service.client.ServiceProxyFactory;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.dao.BlogDao;

import com.hoppinzq.service.interfaceService.CutWordService;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.util.RedisUtils;
import com.hoppinzq.service.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApiServiceMapping(title = "博客服务", description = "博客服务，已加入网关",roleType = ApiServiceMapping.RoleType.NO_RIGHT)
public class BlogService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private RedisUtils redisUtils;


    //@ServiceLimit(limitType = ServiceLimit.LimitType.IP)
    @ApiMapping(value = "getBlogClass", title = "获取博客类别", description = "获取的是类别树，从redis里获取，找不到则兜底从数据库获取并存入redis")
    public JSONObject getBlogClass(Long userId) {
        JSONArray blogClassArray=new JSONArray();
        Object redisBlogClass=redisUtils.get("blogClass");
        if(redisBlogClass==null){
            List<Map> blogClassMap=blogDao.queryBlogClass();
            blogClassArray=JSONArray.parseArray(JSON.toJSONString(blogClassMap));
            redisUtils.set("blogClass",blogClassArray);
        }else{
            blogClassArray=(JSONArray)redisBlogClass;
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",userId);
        jsonObject.put("age",blogClassArray);
        return jsonObject;
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "insertBlog", title = "博客新增", description = "新增博客，有则加之",roleType = ApiMapping.RoleType.LOGIN)
    public void insertBlog(Blog blog) {
        blog.setId(UUIDUtil.getUUID());
        try{
            blog.decode();
            blogDao.insertBlog(blog);
        }catch (Exception ex){
            throw new RuntimeException("新增博客失败::"+ex);
        }
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP)
    @ApiMapping(value = "queryBlog", title = "查询博客", description = "查询所有博客")
    public List<Blog> queryBlog(Blog blog) {

        UserPrincipal upp = new UserPrincipal("zhangqi", "123456");
//        CutWordService service= ServiceProxyFactory.createProxy(CutWordService.class, "http://localhost:8803/service", upp);
//        List<String> list=service.cut("我是猪");
//        System.err.println(list);
//        LoginService loginService=ServiceProxyFactory.createProxy(LoginService.class, "http://localhost:8804/service", upp);
//        loginService.login();

        List<Blog> blogs=new ArrayList<>();
        try{
            blogs=blogDao.queryBlog();
        }catch (Exception ex){
            throw new RuntimeException("查询博客失败:"+ex);
        }
        return blogs;
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "updateBlog", title = "博客更新", description = "更新博客")
    public void updateBlog(Blog blog) {
        try{
            blogDao.updateBlog(blog);
        }catch (Exception ex){
            throw new RuntimeException("更新博客失败:"+ex);
        }
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "deleteBlog", title = "博客删除", description = "删除博客")
    public void deleteBlog(String id) {
        try{
            blogDao.deleteBlog(id);
        }catch (Exception ex){
            throw new RuntimeException("删除博客失败:"+ex);
        }
    }



    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogInsert", title = "博客测试", description = "博客测试")
    public JSONArray blogInsert(List<LinkedHashMap> formInfos) throws IOException, ClassNotFoundException{
        ObjectMapper mapper=new ObjectMapper();
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<formInfos.size();i++){
            JSONObject jsonObject=new JSONObject();
            FormInfo formInfo = mapper.convertValue(formInfos.get(i), FormInfo.class);
            jsonObject.put("name",formInfo.getInputStream());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject1=new JSONObject();
        jsonArray.add(jsonObject1);
        return jsonArray;
    }

//
//    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
//    @ApiMapping(value = "blogImgUpload", title = "博客图片上传", description = "博客图片上传")
//    public JSONObject blogImgUpload(List<LinkedHashMap> formInfos,String blogType) throws IOException, ClassNotFoundException{
//        ObjectMapper mapper=new ObjectMapper();
//        String fileName="";
//        for(int i=0;i<formInfos.size();i++){
//            FormInfo formInfo = mapper.convertValue(formInfos.get(i), FormInfo.class);
//            InputStream inputStream= Base64Util.baseToInputStream(formInfo.getInputStream());
//            fileName=formInfo.getSubmittedFileName();
//            FileUtil.saveFile(inputStream,fileName,"D:\\projectFile\\markdown");
//        }
//        JSONObject jsonObject=new JSONObject();
//        if("markdown".equals(blogType)){
//            jsonObject.put("success",1);
//            jsonObject.put("message","上传成功");
//            jsonObject.put("url","http://127.0.0.1:8809/markdown/"+fileName);
//        }else if("fwb".equals(blogType)){
//            JSONArray jsonArray=new JSONArray();
//            jsonArray.add("http://127.0.0.1:8809/markdown/"+fileName);
//            jsonObject.put("errno",0);
//            jsonObject.put("data",jsonArray);
//        }
//        return jsonObject;
//    }

}
