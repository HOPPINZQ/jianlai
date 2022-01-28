package com.hoppinzq.service.bean;

/**
 * @author: zq
 */
public class SpiderLink {
    String title;
    String link;

    public SpiderLink(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
