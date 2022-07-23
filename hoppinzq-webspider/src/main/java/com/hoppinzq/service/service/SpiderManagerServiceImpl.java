package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiCache;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.bean.SpiderBean;
import com.hoppinzq.service.bean.SpiderMajor;
import com.hoppinzq.service.dao.SpiderDao;
import com.hoppinzq.service.interfaceService.SpiderManagerService;
import com.hoppinzq.service.processor.BuildSpider;
import com.hoppinzq.service.util.SnowflakeIdWorker;
import com.hoppinzq.service.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zq
 */
@ApiServiceMapping(title = "爬虫管理", description = "爬虫管理",roleType = ApiServiceMapping.RoleType.NO_RIGHT)
public class SpiderManagerServiceImpl implements SpiderManagerService {

    @Autowired
    private SpiderDao spiderDao;
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    @ApiMapping(value = "insertSpiders", title = "新增爬虫", description = "新增爬虫")
    public JSONObject insertSpiders(SpiderMajor spiderMajor, List<Map> spiderBeans) {
        long id= snowflakeIdWorker.getSequenceId();
        spiderMajor.setId(id);
        spiderDao.insertSpiderMajor(spiderMajor);
        List<SpiderBean> list=new ArrayList<>();
        for(int i=0;i<spiderBeans.size();i++){
            Map map=spiderBeans.get(i);
            SpiderBean spiderBean=new SpiderBean(spiderMajor.getId(),map);
            list.add(spiderBean);
        }
        spiderDao.insertSpiders(list);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",id+"");
        if(!StringUtils.isBlank(spiderMajor.getUrldemo())){
            jsonObject.put("url",spiderMajor.getUrldemo());
        }
        return jsonObject;
    }

    @ApiMapping(value = "test", title = "测试爬虫", description = "测试爬虫")
    public JSONObject test(SpiderMajor spiderMajor, List<Map> spiderBeans) {
        List<SpiderBean> list=new ArrayList<>();
        for(int i=0;i<spiderBeans.size();i++){
            Map map=spiderBeans.get(i);
            SpiderBean spiderBean=new SpiderBean(spiderMajor.getId(),map);
            list.add(spiderBean);
        }
        BuildSpider buildSpider=new BuildSpider(list);
        Spider.create(buildSpider)
                .addUrl(spiderMajor.getUrldemo())
                .thread(spiderMajor.getThreadNum())
                .run();
        Map map=buildSpider.getResult();
        return new JSONObject(map);
    }

    //缓存24小时
    @ApiCache(time = 60*60*24)
    @Override
    @ApiMapping(value = "run", title = "执行爬虫", description = "执行爬虫")
    public JSONObject getSpiderMessage(Long id, String url) {
        if(id==0||url==null){
            throw new RuntimeException("传参错误！");
        }
        SpiderMajor spiderMajor= spiderDao.querySpiderMajorById(id);
        if(spiderMajor==null){
            throw new RuntimeException(id+"对应的爬虫不存在");
        }
        List<SpiderBean> spiderBeans= spiderDao.querySpidersByMid(id);
        if(spiderBeans.size()==0){
            throw new RuntimeException(id+"对应的爬虫配置不存在");
        }
        BuildSpider buildSpider=new BuildSpider(spiderBeans);
        Spider.create(buildSpider)
                .addUrl(url)
                .thread(spiderMajor.getThreadNum())
                .run();
        Map map=buildSpider.getResult();
        return new JSONObject(map);
    }

    @Override
    @ApiMapping(value = "query", title = "查询爬虫", description = "查询爬虫")
    public SpiderMajor querySpiderById(Long id) {
        SpiderMajor spiderMajor=new SpiderMajor();
        spiderMajor.setId(id);
        List<SpiderMajor> spiderMajors= spiderDao.querySpider(spiderMajor);
        if(spiderMajors==null){
            return null;
        }
        return spiderMajors.get(0);
    }

    @ApiMapping(value = "queryAll", title = "查询所有爬虫", description = "查询所有爬虫")
    public List<SpiderMajor> queryAllSpider() {
        return spiderDao.querySpiderMajorAll();
    }

    @ApiMapping(value = "feedback", title = "反馈", description = "反馈")
    public void insertFeedback(String name,String contact,String message) {
        spiderDao.insertFeedback(name,contact,message);
    }
}
