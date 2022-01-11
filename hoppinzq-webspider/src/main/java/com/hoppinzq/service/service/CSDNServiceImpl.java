package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.aop.annotation.Timeout;
import com.hoppinzq.service.bean.CSDNBlog;
import com.hoppinzq.service.bean.WebMessageContext;
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
    //@Timeout(timeout = 500)
    @ApiMapping(value = "csdnFindMessage", title = "爬取文集", description = "爬取文集")
    public JSONObject getCSDNBlogMessage(String url) {
        try {
            CSDNBlog csdnBlog=new CSDNBlog();
            WebMessageContext.enter(csdnBlog);
            Spider.create(new CSDNProcessor()).addUrl(url).thread(1).run();
            return JSONObject.parseObject(JSON.toJSONString(csdnBlog));
        } catch (Exception ex) {
            logger.error("csdn爬虫服务异常:"+ex.getMessage());
            throw new RuntimeException("csdn爬虫服务异常:"+ex.getMessage());
        } finally {
            WebMessageContext.exit();
        }
    }
}