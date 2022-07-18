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

import static com.hoppinzq.service.util.StringUtils.isBlank;

/**
 * @author: zq
 */
public class GithubRepoPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        SpiderBean spiderBean1=new SpiderBean();
        spiderBean1.setKey("name");
        spiderBean1.setDescription("链接关键字");
        spiderBean1.setXpath("//*[@id=\"content\"]/h1");
        spiderBean1.setXpathFunction("text()");

        SpiderBean spiderBean2=new SpiderBean();
        spiderBean2.setKey("html");
        spiderBean2.setDescription("内容");
        spiderBean2.setXpath("//*[@id=\"content\"]/div[1]/p[1]");
        spiderBean2.setXpathFunction("html()");

        SpiderBean spiderBean3=new SpiderBean();
        spiderBean3.setKey("links");
        spiderBean3.setDescription("链接");
        spiderBean3.setSelector("div.design>a");
        //spiderBean3.setAttr("href");
        spiderBean3.setAll(true);
        spiderBean3.setLinks(true);

        List<SpiderBean> spiderBeans=new ArrayList<>();
        spiderBeans.add(spiderBean1);
        spiderBeans.add(spiderBean2);
        spiderBeans.add(spiderBean3);
        // 部分二：定义如何抽取页面信息，并保存下来
        for (SpiderBean s:spiderBeans) {
            if(!StringUtils.isBlank(s.getXpath())){
                Selectable selectable=page.getHtml().xpath(s.getXpath()+"/"+s.getXpathFunction());
                if(!StringUtils.isBlank(s.getRegex())){
                    selectable.regex(s.getRegex());
                    if(s.getLinks()){
                        if(s.getAll()){
                            page.putField(s.getKey(),page.getHtml().xpath(s.getXpath()+"/"+s.getXpathFunction())
                                    .regex(s.getRegex()).links().all());
                            continue;
                        }
                        selectable.links();
                    }
                    if(s.getAll()){
                        page.putField(s.getKey(),page.getHtml().xpath(s.getXpath()+"/"+s.getXpathFunction())
                                .regex(s.getRegex()).all());
                        continue;
                    }
                }
                if(s.getLinks()){
                    if(s.getAll()){
                        page.putField(s.getKey(),page.getHtml().xpath(s.getXpath()+"/"+s.getXpathFunction())
                                .links().all());
                        continue;
                    }
                    selectable.links();
                }
                page.putField(s.getKey(),selectable.get());

            }else if(!StringUtils.isBlank(s.getSelector())){
                Selectable selectable=page.getHtml().css(s.getSelector());
                if(!StringUtils.isBlank(s.getRegex())){
                    selectable.regex(s.getRegex());
                    if(s.getLinks()){
                        if(s.getAll()){
                            page.putField(s.getKey(),page.getHtml().css(s.getSelector())
                                    .regex(s.getRegex()).links().all());
                            continue;
                        }
                        selectable.links();
                    }
                    if(s.getAll()){
                        page.putField(s.getKey(),page.getHtml().css(s.getSelector())
                                .regex(s.getRegex()).all());
                        continue;
                    }
                }
                if(s.getLinks()){
                    if(s.getAll()){
                        page.putField(s.getKey(),page.getHtml().css(s.getSelector())
                                .links().all());
                        continue;
                    }
                    selectable.links();
                }
                page.putField(s.getKey(),selectable);
            }
        }
//        page.putField("name", page.getHtml().xpath("//*[@id=\"content\"]/h1/text()").toString());
//        List<String> urls = page.getHtml().css("div.design>a").links().all();
        //page.putField("links1", page.getHtml().css("div.design>a").links().all());
        //System.out.println(urls);
        //page.addTargetRequests(urls);
        System.err.println(page.getResultItems().get("name").toString());
        System.err.println(page.getResultItems().get("html").toString());
        System.err.println(page.getResultItems().get("links").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new GithubRepoPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://www.runoob.com/lua/lua-tutorial.html")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
