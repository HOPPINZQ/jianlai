package com.hoppinzq.service.spider;

import com.heaton.bot.*;

public class Searcher
        implements ISpiderReportable {
    public static void main(String[] args) throws Exception {
        try {
            IWorkloadStorable wl = new SpiderInternalWorkload();
            Searcher _searcher = new Searcher();
            Spider _spider
                    = new Spider(_searcher, "https://blog.csdn.net/qq_41544289/article/details/118894495",
                    new HTTPSocket(), 100, wl);
            _spider.setMaxBody(100);
            _spider.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // 发现内部连接时调用，url表示程序发现的URL，若返回true则加入作业中，否则不加入。
    public boolean foundInternalLink(String url) {
        return false;
    }
    // 发现外部连接时调用，url表示程序所发现的URL，若返回true则把加入作业中，否则不加入。
    public boolean foundExternalLink(String url) {
        return false;
    }
    // 当发现其他连接时调用这个方法。其他连接指的是非HTML网页，可能是E-mail或者FTP
    public boolean foundOtherLink(String url) {
        return false;
    }
    // 用于处理网页，这是Spider程序要完成的实际工作。
    public void processPage(HTTP http) {
        System.out.println("扫描网页：" + http.getURL());
        new HTMLParse(http).start();
    }
    // 用来请求一个被处理的网页。
    public void completePage(HTTP http, boolean error) {
    }
    // 由Spider程序调用以确定查询字符串是否应删除。如果队列中的字符串应当删除，方法返回真。
    public boolean getRemoveQuery() {
        return true;
    }
    // 当Spider程序没有剩余的工作时调用这个方法。
    public void spiderComplete() {
    }
}
