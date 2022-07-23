package com.hoppinzq.service.processor;

import com.hoppinzq.service.bean.SpiderBean;
import com.hoppinzq.service.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zq
 */
public class BuildSpider implements PageProcessor {

    private List<SpiderBean> spiderBeans;
    private Map result;

    public Map getResult() {
        return result;
    }
    public BuildSpider(List<SpiderBean> spiderBeans) {
        this.spiderBeans = spiderBeans;
    }

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        result=new HashMap();
        for (SpiderBean s:spiderBeans) {
            if(!StringUtils.isBlank(s.getXpath())){
                String xpath=s.getXpath();
                if(!StringUtils.isBlank(s.getXpathFunction())){
                    xpath+="/"+s.getXpathFunction();
                }
                Selectable selectable=page.getHtml().xpath(xpath);
                if(!StringUtils.isBlank(s.getRegex())){
                    selectable.regex(s.getRegex());
                    if(s.getLinks()){
                        if(s.getAll()){
                            page.putField(s.getKey(),page.getHtml().xpath(xpath)
                                    .regex(s.getRegex()).links().all());
                            continue;
                        }
                        selectable.links();
                    }
                    if(s.getAll()){
                        page.putField(s.getKey(),page.getHtml().xpath(xpath)
                                .regex(s.getRegex()).all());
                        continue;
                    }
                }
                if(s.getLinks()){
                    if(s.getAll()){
                        page.putField(s.getKey(),page.getHtml().xpath(xpath)
                                .links().all());
                        continue;
                    }
                    selectable.links();
                }
                if(s.getAll()){
                    page.putField(s.getKey(),page.getHtml().xpath(xpath).all());
                    continue;
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
                if(s.getAll()){
                    page.putField(s.getKey(),page.getHtml().css(s.getSelector()).all());
                    continue;
                }
                page.putField(s.getKey(),selectable);
            }

        }
        for (SpiderBean s:spiderBeans) {
            String key=s.getKey();
            result.put(key,page.getResultItems().get(key));
        }
    }
    @Override
    public Site getSite() {
        return site;
    }
}
