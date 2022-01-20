//package com.hoppinzq.service.controller;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.cn.ChineseAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.Hits;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//
//@RestController
//public class SearchController {
//
//    @GetMapping(value = "/search")
//    @ResponseBody
//    public void search(String QC, HttpServletResponse response) throws Exception{
//        if (QC == null) {
//            QC = "";
//        }
//        response.setContentType("text/html; charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
//            Search(QC, out);
//        }
//        catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//    }
//
//    public void Search(String qc, PrintWriter out) throws Exception {
//        // 从索引目录创建索引
//        IndexSearcher _searcher = new IndexSearcher("D:\\index");
//        // 创建标准分析器
//        Analyzer analyzer = new ChineseAnalyzer();
//        // 查询条件
//        String line = qc;
//        // Query是一个抽象类
//        Query query = QueryParser.parse(line, "title", analyzer);
//
//        out.println("<html>");
//        out.println("<head><title>搜索结果</title></head>");
//        out.println("<body bgcolor=#ffffff>");
//        out.println("<center>" +
//                "<form action='/search' method='get'>" +
//                "<font face='华文中宋' color='#3399FF'>搜索引擎</font>:" +
//                "<input type='text' name='QC' size='20'>" +
//                "<input type='submit' name='submit' value='开始搜索'>" +
//                "</form></center>"
//        );
//        out.println("<p>搜索关键字：<font color=red>" + query.toString("title") +
//                "</font></p>");
//        Hits hits = _searcher.search(query);
//        out.println(" 总共找到<font color=red>" + hits.length() + "</font>条记录<br>");
//
//        final int HITS_PER_PAGE = 10;
//        for (int start = 0; start < hits.length(); start += HITS_PER_PAGE) {
//            int end = Math.min(hits.length(), start + HITS_PER_PAGE);
//            for (int i = start; i < end; i++) {
//                Document doc = hits.doc(i);
//                String url = doc.get("url");
//                if (url != null) {
//                    out.println( (i + 1) + " <a target='_blank' href='" + url + "'>" +
//                            replace(doc.get("title"), qc) +
//                            "</a><br>");
//                }
//                else {
//                    System.out.println("没有找到！");
//                }
//            }
//        }
//        out.println("</body></html>");
//        _searcher.close();
//    };
//
//    public String input(String str) {
//        String temp = null;
//        if (str != null) {
//            try {
//                temp = new String(str.getBytes("ISO-8859-1"),"UTF-8");
//            }
//            catch (Exception e) {
//            }
//        }
//        return temp;
//    }
//
//    public String replace(String title, String keyword) {
//        return title.replaceAll(keyword, "<font color='red'>" + keyword + "</font>");
//    };
//}
