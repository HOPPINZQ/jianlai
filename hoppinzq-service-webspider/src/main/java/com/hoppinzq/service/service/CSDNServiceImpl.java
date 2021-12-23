package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.CSDNBlog;
import com.hoppinzq.service.interfaceService.CSDNService;
import com.hoppinzq.service.processor.CSDNProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import us.codecraft.webmagic.Spider;

/**
 * @author: zq
 */
@ServiceRegister
@ApiServiceMapping(title = "csdn爬虫服务", description = "csdn爬虫服务",roleType = ApiServiceMapping.RoleType.NO_RIGHT)
public class CSDNServiceImpl implements CSDNService {
    private static final Logger logger = LoggerFactory.getLogger(CSDNServiceImpl.class);

    @Value("${zqRedis.csdnBlogTimeout:60*60}")
    private int redisCSDNBlogTimeout;

    @Override
    @ApiMapping(value = "csdnFindMessage", title = "爬取文集", description = "爬取文集")
    public JSONObject getCSDNBlogMessage(String url) {
        //"https://blog.csdn.net/qq_41544289/article/details/119749268"
        try {
            Spider.create(new CSDNProcessor()).addUrl(url).thread(1).run();
            return JSONObject.parseObject(JSON.toJSONString(collect()));
        } catch (Exception ex) {
            logger.error("csdn爬虫服务异常:"+ex.getMessage());
            throw new RuntimeException("csdn爬虫服务异常:"+ex.getMessage());
        } finally {
            CSDNBlog.exit();
        }
    }

    private static CSDNBlog collect(){
        return new CSDNBlog(CSDNBlog.title,CSDNBlog.author,CSDNBlog.date,CSDNBlog.html,CSDNBlog.text,
                CSDNBlog.classType,CSDNBlog.is_create_self,CSDNBlog.url,CSDNBlog.like,CSDNBlog.collect);
    }
}
