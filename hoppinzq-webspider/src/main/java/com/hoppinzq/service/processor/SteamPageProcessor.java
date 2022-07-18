package com.hoppinzq.service.processor;

import com.hoppinzq.service.bean.SpiderBean;
import com.hoppinzq.service.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zq
 */
public class SteamPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        int index=7;
        String gameUrl=page.getHtml().xpath("//*[@id=\"NewReleasesRows\"]/a["+index+"]").links().toString();
        String gameName=page.getHtml().xpath("//*[@id=\"NewReleasesRows\"]/a["+index+"]/div[3]/div[1]/text()").toString();
        String gamePrice=page.getHtml().xpath("//*[@id=\"NewReleasesRows\"]/a["+index+"]/div[2]/div/div/text()").toString();
        String gameImg=page.getHtml().xpath("//*[@id=\"NewReleasesRows\"]/a["+index+"]/div[1]/img/@src").toString();
        String gameClasses=page.getHtml().xpath("//*[@id=\"NewReleasesRows\"]/a["+index+"]/div[3]/div[2]/div/tidyText()").toString();;
        System.err.println("gameUrl:"+gameUrl+"\ngameName:"+gameName+"\ngamePrice:"+gamePrice+"\ngameImg:"+gameImg+"\ngameClasses:"+gameClasses);

        List<String> urls=new ArrayList<>();
        urls.add("https://store.steampowered.com/category/action/#p=8&tab=NewReleases");
        page.addTargetRequests(urls);

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new SteamPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://store.steampowered.com/category/action/#p=0&tab=NewReleases")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
