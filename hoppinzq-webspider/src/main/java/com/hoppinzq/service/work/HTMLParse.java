package com.hoppinzq.service.work;


import com.hoppinzq.service.bean.SpiderLink;
import com.hoppinzq.service.cache.BloomFilterCache;
import com.hoppinzq.service.html.HTMLPage;
import com.hoppinzq.service.html.HTTP;
import com.hoppinzq.service.html.Link;
import com.hoppinzq.service.util.EmojiConvert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.*;

public class HTMLParse {
    @Value("${lucene.spiderIndex:}")
    private String indexPath;

    HTTP _http = null;
    public HTMLParse(HTTP http) {
        _http = http;
    }

    /**
     * 对Web页面进行解析后建立索引
     */
    public void start() {
        try {
            HTMLPage _page = new HTMLPage(_http);
            _page.open(_http.getURL(), null);
            Vector _links = _page.getLinks();
            List<Document> docList = new ArrayList<>();
            Iterator _it = _links.iterator();
            int n = 0;
            while (_it.hasNext()) {
                Link _link = (Link) _it.next();
                String _href = input(_link.getHREF().trim());
                int index = _href.indexOf("?");
                if (index != -1)
                    _href = _href.substring(0, index);
                if(_link.getPrompt()!=null){
                    String _title = input(_link.getPrompt().trim());
                    if(_title.length()>0&&!BloomFilterCache.urlIndexFilter.mightContain(_href)){
                        BloomFilterCache.urlIndexFilter.put(_href);
                        BloomFilterCache.urls.add(new SpiderLink(_title,_href));
                        System.out.println("标题:"+_title+",链接:"+_href+" 已经被加入到索引库内。");
                        Document document = new Document();
                        document.add(new TextField("title", _title, Field.Store.YES));
                        document.add(new TextField("link", _href, Field.Store.YES));
                        docList.add(document);
                    }
                    n++;
                }
            }
            System.out.println("共扫描到" + n + "个链接");
            Analyzer analyzer = new IKAnalyzer();
            Directory  dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);

            for (Document doc : docList) {
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * 解决java中的中文问题
     * @param str 输入的中文
     * @return 经过解码的中文
     */
    public static String input(String str) {
        String temp = null;
        if (str != null) {
            try {
                temp = new String(str.getBytes("ISO-8859-1"),"UTF-8");
            }
            catch (Exception e) {
            }
        }
        return temp;
    }
}
