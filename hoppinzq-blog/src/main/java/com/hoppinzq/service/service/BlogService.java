package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppinzq.service.aop.Self;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceLimit;
import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.bean.FormInfo;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.dao.BlogDao;

import com.hoppinzq.service.util.JSONUtil;
import com.hoppinzq.service.util.RedisUtils;
import com.hoppinzq.service.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@ApiServiceMapping(title = "博客服务", description = "博客服务，已加入网关",roleType = ApiServiceMapping.RoleType.RIGHT)
public class BlogService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private RedisUtils redisUtils;
    private BlogService blogService;

    @Self
    public void setSelf(BlogService blogService) {
        this.blogService = blogService;
    }

    private static final String blog2RedisKeyPrefix="BLOG:";
    private static final String blog2RedisBlogId=blog2RedisKeyPrefix+"BLOG_ID:";
    private static final String blog2RedisBlogClass=blog2RedisKeyPrefix+"BLOG_CLASS:";
    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

    /**
     * 草稿保存进redis
     * 流程，前端每30s调用一次该接口，从blog的text取到前端处理好的数据，然后清空一些内容，减少对数据库的压力
     * 1、第一次进入该接口，redis肯定没有该id的草稿,异步将草稿存入数据库内
     * 2、若redis有该id的草稿，覆盖之，返回ID
     * @return
     */
    @ApiMapping(value = "saveBlog2Redis", title = "保存草稿", description = "每1min会调用一次接口保存博客内容进redis",roleType = ApiMapping.RoleType.LOGIN)
    public JSONObject saveBlog2Redis(Blog blog){
        String blogId=blog.getId();
        blog.decode();
        JSONObject returnJSON = JSONUtil.createJSONObject("blogId",blogId);
        JSONObject saveJSON=(JSONObject)redisUtils.get(blog2RedisBlogId+blogId);
        if(saveJSON==null){
            returnJSON.put("isNew",true);
            blog.setType(0);
            blogService.insertBlog(blog);
            saveJSON=new JSONObject();
        }else{
            returnJSON.put("isNew",false);
        }
        saveJSON=JSONObject.parseObject(JSONObject.toJSONString(blog));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate=new Date();
        Long now=nowDate.getTime();
        saveJSON.put("lastUpdateTime",now);
        Boolean isRedis=redisUtils.set(blog2RedisBlogId+blogId,saveJSON);
        if(!isRedis){
            logger.error("草稿保存错误");
            throw new RuntimeException("草稿保存错误！");
        }
        returnJSON.put("lastUpdateTime",simpleDateFormat.format(nowDate));
        return returnJSON;
    }



    //@ServiceLimit(limitType = ServiceLimit.LimitType.IP)
    @ApiMapping(value = "getBlogClass", title = "获取博客类别", description = "获取的是类别树，从redis里获取，找不到则兜底从数据库获取并存入redis")
    public JSONObject getBlogClass(Long userId) {
        JSONArray blogClassArray=new JSONArray();
        Object redisBlogClass=redisUtils.get(blog2RedisBlogClass+"blogClass");
        if(redisBlogClass==null){
            List<Map> blogClassMap=blogDao.queryBlogClass();
            blogClassArray=JSONArray.parseArray(JSON.toJSONString(blogClassMap));
            redisUtils.set(blog2RedisBlogClass+"blogClass",blogClassArray);
        }else{
            blogClassArray=(JSONArray)redisBlogClass;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",userId);
        jsonObject.put("age",blogClassArray);
        return jsonObject;
    }


    @Async
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "insertBlog", title = "博客新增", description = "新增博客，有则加之",roleType = ApiMapping.RoleType.LOGIN)
    public void insertBlog(Blog blog) {
        if(blog.getId()==null){
            blog.setId(UUIDUtil.getUUID());
        }
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

    @Async
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "updateBlog", title = "博客更新", description = "更新博客",roleType = ApiMapping.RoleType.LOGIN)
    public void updateBlog(Blog blog) {
        try{
            blogDao.updateBlog(blog);
        }catch (Exception ex){
            throw new RuntimeException("更新博客失败:"+ex);
        }
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "deleteBlog", title = "博客删除", description = "删除博客",roleType = ApiMapping.RoleType.LOGIN)
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
