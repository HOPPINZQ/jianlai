package com.hoppinzq.service.runner;

import com.hoppinzq.service.bean.BlogVo;
import com.hoppinzq.service.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CacheRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private BlogService blogService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.debug("开始预热缓存");
        //类被入redis
        blogService.getBlogClass();
        //10个热门
        BlogVo.BuilderBlogVo blogVoHot=BlogVo.newBuilder().searchType(0)
                .blogReturn(1).order(4).pageIndex(1).pageSize(10);
        blogService.getHotBlog(blogVoHot.bulid());
        blogService.getHotBlogClass(10);
        logger.debug("预热缓存结束");
    }
}