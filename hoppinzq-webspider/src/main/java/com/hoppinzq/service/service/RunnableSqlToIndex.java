package com.hoppinzq.service.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author: zq
 */
public class RunnableSqlToIndex implements Runnable {
    private Thread t;
    private String threadName;
    private List<Map> mapList;

    RunnableSqlToIndex(String name,List<Map> list) {
        threadName = name;
        mapList=list;
        System.out.println("创建 " +  threadName );
    }

    public void run() {
        System.out.println("执行 " +  threadName );
        try {
            Document document = new Document();
            Analyzer analyzer = new IKAnalyzer();
            Directory dir = FSDirectory.open(Paths.get("D:\\indexindex"));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            for (Map m:mapList) {
                if(m.get("link").toString().length()!=0){
                    document.add(new TextField("title", m.get("title").toString(), Field.Store.YES));
                    document.add(new TextField("link", m.get("link").toString(), Field.Store.YES));
                    System.out.println(m.get("id").toString());
                    indexWriter.addDocument(document);
                }

            }
            indexWriter.close();
        }catch (Exception e) {
            System.out.println("Thread " +  threadName + " 中断.");
        }
        System.out.println("Thread " +  threadName + " 退出.");
    }

    public void start () {
        System.out.println("开启线程 " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}
