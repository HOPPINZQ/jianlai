package com.hoppinzq.service.processor;

import com.hoppinzq.service.bean.CSDNBlog;
import com.hoppinzq.service.bean.WebMessageContext;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zq
 */
public class OtherProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {

        String blogTitle=page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]/span/text()").toString();
        String blogText=page.getHtml().xpath("//*[@id=\"cnblogs_post_body\"]/tidyText()").toString();
        String blogHtml=page.getHtml().xpath("//*[@id=\"cnblogs_post_body\"]/html()").toString();
        String blogAuthor=page.getHtml().xpath("//*[@id=\"topics\"]/div/div[3]/a[1]/text()").toString();
        int is_create_self=0;
        List<String> blogClass=new ArrayList<String>();
        String xpathClassA="//*[@id=\"BlogPostCategory\"]/a/text()";
        String blogClassText=page.getHtml().xpath(xpathClassA).toString();
        blogClass.add(blogClassText);

        String blogDate=page.getHtml().xpath("//*[@id=\"post-date\"]/html()").toString();
        String like="0";
        String collect="0";
        //
        CSDNBlog csdnBlog=(CSDNBlog) WebMessageContext.getPrincipal();
        csdnBlog.setCSDNBlog(blogTitle,blogAuthor,blogDate,blogHtml,blogText,blogClass,is_create_self,page.getUrl().toString(),like,collect);
    }

    @Override
    public Site getSite() {
        return site;
    }
}
