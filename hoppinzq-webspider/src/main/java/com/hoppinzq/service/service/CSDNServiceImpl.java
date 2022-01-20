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
import com.hoppinzq.service.processor.CNBlogProcessor;
import com.hoppinzq.service.processor.CSDNProcessor;
import com.hoppinzq.service.processor.WeChatProcessor;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientGenerator;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
    public JSONObject getCSDNBlogMessage(String url,int type) throws NoSuchAlgorithmException, KeyManagementException {
//        SSLContext sc =SSLContext.getInstance("SSL");
//        sc.init((KeyManager[])null, InsecureTrustManagerFactory.INSTANCE.getTrustManagers(), (SecureRandom)null);
        try {
            CSDNBlog csdnBlog=new CSDNBlog();
            WebMessageContext.enter(csdnBlog);
            if(type==1){
                Spider.create(new CSDNProcessor()).addUrl(url).thread(1).run();
            }else if(type==2){
                Spider.create(new CNBlogProcessor()).addUrl(url).thread(1).run();
            }else if(type==3){
                Spider.create(new WeChatProcessor()).addUrl(url).thread(1).run();
            }
            return JSONObject.parseObject(JSON.toJSONString(csdnBlog));
        } catch (Exception ex) {
            logger.error("csdn爬虫服务异常:"+ex.getMessage());
            throw new RuntimeException("csdn爬虫服务异常:"+ex.getMessage());
        } finally {
            WebMessageContext.exit();
        }
    }
}
