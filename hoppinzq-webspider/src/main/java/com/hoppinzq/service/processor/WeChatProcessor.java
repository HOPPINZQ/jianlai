package com.hoppinzq.service.processor;

import com.hoppinzq.service.bean.CSDNBlog;
import com.hoppinzq.service.bean.WebMessageContext;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: zq
 */
@Component
public class WeChatProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        String blogTitle=page.getHtml().xpath("//*[@id=\"activity-name\"]/text()").toString();
        String blogText=page.getHtml().xpath("//*[@id=\"js_content\"]/tidyText()").toString();
        String blogHtml=page.getHtml().xpath("//*[@id=\"js_content\"]/html()").toString();
        String blogAuthor=page.getHtml().xpath("//*[@id=\"js_name\"]/text()").toString();
        int is_create_self=0;
        List<String> blogClass= Collections.EMPTY_LIST;

        String blogDate=page.getHtml().xpath("//*[@id=\"publish_time\"]/text()").toString();
        String like="0";
        String collect="0";
        CSDNBlog csdnBlog=(CSDNBlog) WebMessageContext.getPrincipal();
        csdnBlog.setCSDNBlog(blogTitle,blogAuthor,blogDate,blogHtml,blogText,blogClass,is_create_self,page.getUrl().toString(),like,collect);
    }

    @Override
    public Site getSite() {
        return site;
    }
}
