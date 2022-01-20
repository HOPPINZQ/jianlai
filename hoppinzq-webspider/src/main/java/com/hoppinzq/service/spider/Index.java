//package com.hoppinzq.service.spider;
//
//
//public class Index {
//
//    IndexWriter _writer = null;
//    Index() throws Exception {
//        _writer = new IndexWriter("D:\\index",
//                new ChineseAnalyzer(), true);
//    }
//
//    /**
//     * 把每条新闻加入索引中
//     * @param url 新闻的url
//     * @param title 新闻的标题
//     * @throws Exception
//     */
//    void AddNews(String url, String title) throws Exception {
//        Document _doc = new Document();
//        _doc.add(Field.Text("title", title));
//        _doc.add(Field.UnIndexed("url", url));
//        _writer.addDocument(_doc);
//    }
//
//    /**
//     * 优化并且清理资源
//     * @throws Exception
//     */
//    void close() throws Exception {
//        _writer.optimize();
//        _writer.close();
//    }
//}
