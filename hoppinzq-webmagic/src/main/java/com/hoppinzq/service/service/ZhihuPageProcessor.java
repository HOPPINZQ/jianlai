package com.hoppinzq.service.service;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zq
 */
@Component
public class ZhihuPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        //page.addTargetRequests(page.getHtml().links().regex("https://www\\.zhihu\\.com/question/\\d+/answer/\\d+.*").all());
        String blogTitle=page.getHtml().xpath("//*[@id=\"articleContentId\"]/html()").toString();
        String blogText=page.getHtml().xpath("//*[@id=\"content_views\"]/tidyText()").toString();

        String blogHtml=page.getHtml().xpath("//*[@id=\"content_views\"]/html()").toString();
        page.putField("title", blogHtml);
        String blogAuthor=page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div[1]/div/div[2]/div[1]/div/a[1]/html()").toString();
        page.putField("answer", page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div[1]/div/div[2]/div[1]/div/a[1]/tidyText()").toString());

        String imageSrc=page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div[1]/div/div[2]/div[1]/img/@src").toString();
        List blogClass=new ArrayList<>();
        int classIndex=2;
        while (true){
            String xpathClassA="//*[@id=\"mainBox\"]/main/div[1]/div[1]/div/div[2]/div[2]/div/a["+classIndex+"]/text()";
            String blogClassText=page.getHtml().xpath(xpathClassA).toString();
            if(blogClassText!=null){
                blogClass.add(blogClassText);
                classIndex++;
            }else{
                break;
            }
        }
        String blogDate=page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div[1]/div/div[2]/div[1]/div/span[1]/html()").toString();
        if (page.getResultItems().get("title") == null) {
            // 如果是列表页，跳过此页，pipeline不进行后续处理
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
