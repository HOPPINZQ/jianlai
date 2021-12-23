package com.hoppinzq.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: zq
 */
@Component
public class ZhihuTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhihuPipeline.class);

    @Autowired
    private ZhihuPipeline zhihuPipeline;

    @Autowired
    private ZhihuPageProcessor zhihuPageProcessor;

    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    public void crawl() {
        // 定时任务，每10分钟爬取一次
        timer.scheduleWithFixedDelay(() -> {
            Thread.currentThread().setName("hoppinzqThread");

            try {
                Spider.create(zhihuPageProcessor)
                        // 爬我的首页试试
                        .addUrl("https://hoppinzq.com/")
                        // 抓取到的数据存数据库
                        .addPipeline(zhihuPipeline)
                        // 开启2个线程抓取
                        .thread(2)
                        // 异步启动爬虫
                        .start();
            } catch (Exception ex) {
                LOGGER.error("定时抓取知乎数据线程执行异常", ex);
            }
        }, 0, 10, TimeUnit.MINUTES);
    }
}
