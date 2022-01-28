package com.hoppinzq.service.bean;

import java.util.Date;

/**
 * @author: zq
 * 额外信息，可拓展
 */
public class BlogExtra {
    private int id;
    private int setType;
    private long blogId;
    private long author;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSetType() {
        return setType;
    }

    public void setSetType(int setType) {
        this.setType = setType;
    }

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long blogId) {
        this.blogId = blogId;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
