package com.hoppinzq.service.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.SpiderLink;
import com.hoppinzq.service.dao.SpiderDao;
import com.hoppinzq.service.interfaceService.SpiderService;
import com.hoppinzq.service.work.SpiderWoker;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author: zq
 */
@ServiceRegister
@ApiServiceMapping(title = "蜘蛛爬虫", description = "蜘蛛爬虫",roleType = ApiServiceMapping.RoleType.NO_RIGHT)
public class SpiderServiceImpl implements SpiderService {
    @Autowired
    private SpiderWoker spiderWoker;
    @Autowired
    private SpiderDao spiderDao;

    @ApiMapping(value = "startWork", title = "爬取网站链接关键字", description = "爬取网站链接关键字")
    public void startWork(String url){
        spiderWoker.startWork(url);
    }

    @ApiMapping(value = "queryweb", title = "查询网站", description = "根据关键词查询网站")
    public JSONArray queryweb(String search) {
        List<SpiderLink> spiderLinks=spiderWoker.queryweb(search);
        return JSONArray.parseArray(JSONObject.toJSONString(spiderLinks));
    }

    @ApiMapping(value = "sqltoindex", title = "数据库入索引库", description = "数据库入索引库")
    public void sql2index() throws IOException {
        List<Map> lists=spiderDao.queryAllLink();
//        List<Map> list1=spiderDao.queryLink(0,6000);
//        List<Map> list2=spiderDao.queryLink(6000,6000);
//        List<Map> list3=spiderDao.queryLink(12000,6000);
//        List<Map> list4=spiderDao.queryLink(18000,6000);
//        List<Map> list5=spiderDao.queryLink(24000,10000);

//        RunnableSqlToIndex R1 = new RunnableSqlToIndex( "Thread-1",list1);
//        R1.start();
//        RunnableSqlToIndex R2 = new RunnableSqlToIndex( "Thread-2",list2);
//        R2.start();
//        RunnableSqlToIndex R3 = new RunnableSqlToIndex( "Thread-3",list3);
//        R3.start();
//        RunnableSqlToIndex R4 = new RunnableSqlToIndex( "Thread-4",list4);
//        R4.start();
//        RunnableSqlToIndex R5 = new RunnableSqlToIndex( "Thread-5",list5);
//        R5.start();

        Document document = new Document();
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("D:\\indexindex"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        for (Map m:lists) {
            document.add(new TextField("title", m.get("title").toString(), Field.Store.YES));
            document.add(new TextField("link", m.get("link").toString(), Field.Store.YES));
            System.out.println(m.get("id").toString());
            indexWriter.addDocument(document);
        }
        indexWriter.close();
        System.out.println("完成！");
    }

}
