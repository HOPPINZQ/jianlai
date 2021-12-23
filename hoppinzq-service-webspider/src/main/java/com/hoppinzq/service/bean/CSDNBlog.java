package com.hoppinzq.service.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zq
 */
public class CSDNBlog{

    private static ThreadLocal<Serializable> principalHolder = new ThreadLocal<Serializable>();

    public static final Serializable getPrincipal() {
        return principalHolder.get();
    }

    public static final void setPrincipal(Serializable principal) {
        principalHolder.set(principal);
    }

    public static void exit() {
        principalHolder.set(null);
    }

    public static void enter() {
        principalHolder.set(null);
    }
    public static String title;
    public static String author;
    public static String date;
    public static String html;
    public static String text;
    public static List<String> classType;
    public static int is_create_self;//0原创，1转载
    public static String url;
    public static String like;
    public static String collect;

    public CSDNBlog() {
    }

    public CSDNBlog(String title, String author, String date, String html, String text, List<String> classType, int is_create_self, String url, String like, String collect) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.html = html;
        this.text = text;
        this.classType = classType;
        this.is_create_self = is_create_self;
        this.url = url;
        this.like = like;
        this.collect = collect;
    }

    public int getIs_create_self() {
        return is_create_self;
    }

    public void setIs_create_self(int is_create_self) {
        this.is_create_self = is_create_self;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getClassType() {
        return classType;
    }

    public void setClassType(List<String> classType) {
        this.classType = classType;
    }
}
