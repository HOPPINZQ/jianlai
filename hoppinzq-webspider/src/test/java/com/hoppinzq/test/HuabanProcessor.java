//package com.hoppinzq.test;
//
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
//import us.codecraft.webmagic.pipeline.FilePipeline;
//import us.codecraft.webmagic.processor.PageProcessor;
//import us.codecraft.webmagic.scheduler.RedisScheduler;
//
///**
// * 花瓣网抽取器。<br>
// * 使用Selenium做页面动态渲染。<br>
// */
//public class HuabanProcessor implements PageProcessor {
//
//    private Site site;
//
//    @Override
//    public void process(Page page) {
//        page.addTargetRequests(page.getHtml().links().regex("http://huaban\\.com/.*").all());
//        if (page.getUrl().toString().contains("pins")) {
//            page.putField("img", page.getHtml().xpath("//div[@id='pin_img']/img/@src").toString());
//        } else {
//            page.getResultItems().setSkip(true);
//        }
//    }
//
//    @Override
//    public Site getSite() {
//        if (site == null) {
//            site = Site.me().setDomain("huaban.com").addStartUrl("http://huaban.com/").setSleepTime(1000);
//        }
//        return site;
//    }
//
//    public static void main(String[] args) {
//        Spider.create(new HuabanProcessor()).thread(5)
//                .scheduler(new RedisScheduler("localhost"))
//                .pipeline(new FilePipeline("/data/webmagic/test/"))
//                .downloader(new SeleniumDownloader("/Users/yihua/Downloads/chromedriver"))
//                .run();
//    }
//}
