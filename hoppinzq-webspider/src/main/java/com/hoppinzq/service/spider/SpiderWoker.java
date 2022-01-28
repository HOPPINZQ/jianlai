package com.hoppinzq.service.spider;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.heaton.bot.*;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.bean.SpiderLink;
import com.hoppinzq.service.config.WebSocketProcess;
import com.hoppinzq.service.dao.SpiderDao;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zq
 */
@Component
public class SpiderWoker implements ISpiderReportable {
    BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 100000000, 0.0001);
    @Autowired
    private SpiderWoker woker;
    @Autowired
    private SpiderDao spiderDao;
    @Autowired
    private WebSocketProcess webSocketProcess;
    @Value("${lucene.spiderIndex}")
    private String indexPath;

    public void startWork(String url){
        //"https://www.runoob.com"
        try {
            IWorkloadStorable wl = new SpiderInternalWorkload();
            Spider _spider
                    = new Spider(woker, url,
                    new HTTPSocket(), 100, wl);
            _spider.setMaxBody(100);
            _spider.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<SpiderLink> queryweb(String search) {
        List<SpiderLink> spiderLinks=new ArrayList<>();
        try{
            Analyzer analyzer = new IKAnalyzer();
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            QueryParser queryBlogTitleParser = new QueryParser("title", analyzer);
            Query queryTitle = queryBlogTitleParser.parse(search);
            query.add(queryTitle, BooleanClause.Occur.MUST);
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexReader indexReader = DirectoryReader.open(dir);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            TopDocs topDocs = indexSearcher.search(query.build(),100);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            if (scoreDocs != null) {
                for (ScoreDoc scoreDoc : scoreDocs) {
                    //获取查询到的文档唯一标识, 文档id, 这个id是lucene在创建文档的时候自动分配的
                    int docID = scoreDoc.doc;
                    Document doc = indexSearcher.doc(docID);
                    spiderLinks.add(new SpiderLink(doc.get("title"),doc.get("link")));
                }
            }
            indexReader.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return spiderLinks;
    }

    // 发现内部连接时调用，url表示程序发现的URL，若返回true则加入作业中，否则不加入。
    public boolean foundInternalLink(String url) {
        System.err.println("发现内部链接："+url);
        if(bloomFilter.mightContain(url)){
            return false;
        }else{
//            try {
//                webSocketProcess.sendMessage("发现内部链接："+url+"加入成功");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            bloomFilter.put(url);
            return true;
        }
    }

    // 发现外部连接时调用，url表示程序所发现的URL，若返回true则把加入作业中，否则不加入。
    public boolean foundExternalLink(String url) {
        System.err.println("发现外部链接："+url);
        return false;
    }

    // 当发现其他连接时调用这个方法。其他连接指的是非HTML网页，可能是E-mail或者FTP
    public boolean foundOtherLink(String url) {
        System.err.println("发现其他链接："+url);
        return false;
    }

    // 用于处理网页，这是Spider程序要完成的实际工作。
    public void processPage(HTTP http) {
        System.out.println("扫描网页：" + http.getURL());
        new HTMLParse(http).start();
    }

    // 用来请求一个被处理的网页。
    public void completePage(HTTP http, boolean error) {
        System.out.println("completePage");
    }

    // 由Spider程序调用以确定查询字符串是否应删除。如果队列中的字符串应当删除，方法返回真。
    public boolean getRemoveQuery() {
        return true;
    }

    // 当Spider程序没有剩余的工作时调用这个方法。
    public void spiderComplete() {
        spiderDao.insertSpiderLink(BloomFilterCache.urls);
        System.out.println("已结束");
    }
}
