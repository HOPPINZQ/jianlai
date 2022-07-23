package com.hoppinzq.service.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.SpiderLink;
import com.hoppinzq.service.dao.SpiderDao;
import com.hoppinzq.service.interfaceService.SpiderService;
import com.hoppinzq.service.util.EmojiConvert;
import com.hoppinzq.service.work.Worker;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zq
 */
@ServiceRegister
@ApiServiceMapping(title = "蜘蛛爬虫", description = "蜘蛛爬虫",roleType = ApiServiceMapping.RoleType.NO_RIGHT)
public class SpiderServiceImpl implements SpiderService {
    @Autowired
    private Worker worker;
    @Autowired
    private SpiderDao spiderDao;
    @Value("${lucene.spiderIndex:}")
    private String indexPath;

    @ApiMapping(value = "startWork", title = "爬取网站链接关键字", description = "爬取网站链接关键字")
    public void startWork(String url){
        worker.startWork(url);
    }

    @ApiMapping(value = "queryweb", title = "查询网站", description = "根据关键词查询网站")
    public JSONArray queryweb(String search) {
        List<SpiderLink> spiderLinks=worker.queryweb(search);
        for (SpiderLink s:spiderLinks) {
            try{
                s.setTitle(EmojiConvert.utfemojiRecovery(s.getTitle()));
            }catch (Exception ex){

            }
        }
        return JSONArray.parseArray(JSONObject.toJSONString(spiderLinks));
    }

    @ApiMapping(value = "sqltoindex", title = "数据库入索引库", description = "数据库入索引库")
    public void sql2index() throws IOException {
        List<SpiderLink> lists=spiderDao.queryAllLinkNotIndex();
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        List<Document> docList = new ArrayList<>();
        for (SpiderLink spiderLink:lists) {
            if(spiderLink.getTitle()!=null&&spiderLink.getTitle().toString().length()!=0&& spiderLink.getLink()!=null){
                Document document = new Document();
                document.add(new TextField("title", spiderLink.getTitle(), Field.Store.YES));
                document.add(new TextField("link", spiderLink.getLink(), Field.Store.YES));
                docList.add(document);
            }
        }
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        spiderDao.updateLinkInIndex(lists);
        indexWriter.close();
        System.out.println("完成！");
    }
}
