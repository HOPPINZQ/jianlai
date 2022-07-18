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
public class SteamGameDemo1 implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        List<SpiderBean> spiderBeans=new ArrayList<>();
        for(int i=1;i<=15;i++){
            SpiderBean spiderBean1=new SpiderBean();
            spiderBean1.setKey("link");
            spiderBean1.setDescription("游戏地址");
            spiderBean1.setSelector("#NewReleasesRows > a:nth-child("+i+")");
            spiderBean1.setLinks(true);

            SpiderBean spiderBean2=new SpiderBean();
            spiderBean2.setKey("img");
            spiderBean2.setDescription("游戏图片");
            spiderBean2.setSelector("#NewReleasesRows > a:nth-child("+i+") > div.tab_item_cap > img");
            spiderBean2.setAttr("src");

            SpiderBean spiderBean3=new SpiderBean();
            spiderBean3.setKey("name");
            spiderBean3.setDescription("游戏名称");
            spiderBean3.setXpath("//*[@id=\"NewReleasesRows\"]/a["+1+"]/div[3]/div["+i+"]");
            spiderBean3.setXpathFunction("text(0)");

            SpiderBean spiderBean4=new SpiderBean();
            spiderBean4.setKey("class");
            spiderBean4.setDescription("游戏类型");
            spiderBean4.setXpath("//*[@id=\"NewReleasesRows\"]/a["+i+"]/div[3]/div[2]/div");
            spiderBean4.setXpathFunction("text(0)");

            SpiderBean spiderBean5=new SpiderBean();
            spiderBean4.setKey("price");
            spiderBean4.setDescription("游戏价格");
            spiderBean4.setXpath("//*[@id=\"NewReleasesRows\"]/a["+i+"]/div[2]/div/div");
            spiderBean4.setXpathFunction("text(0)");

            spiderBeans.add(spiderBean1);
            spiderBeans.add(spiderBean2);
            spiderBeans.add(spiderBean3);
            spiderBeans.add(spiderBean4);
            spiderBeans.add(spiderBean5);
        }

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
            System.err.println(page.getResultItems().get("link").toString());
            System.err.println(page.getResultItems().get("img").toString());
            System.err.println(page.getResultItems().get("class").toString());
            System.err.println(page.getResultItems().get("price").toString());
            System.err.println(page.getResultItems().get("name").toString());
            System.err.println("---------------------------------");
        }
//        page.putField("name", page.getHtml().xpath("//*[@id=\"content\"]/h1/text()").toString());
//        List<String> urls = page.getHtml().css("div.design>a").links().all();
        //page.putField("links1", page.getHtml().css("div.design>a").links().all());
        //System.out.println(urls);
        //page.addTargetRequests(urls);
//        System.err.println(page.getResultItems().get("name").toString());
//        System.err.println(page.getResultItems().get("html").toString());
//        System.err.println(page.getResultItems().get("links").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new SteamGameDemo1())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://store.steampowered.com/category/action/#p=0&tab=NewReleases")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
