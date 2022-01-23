package com.hoppinzq.service.bean;

import java.util.Date;

/**
 * @author: zq
 * 评论实体类
 */
public class Comment {
    private int id;
    private String author;
    private String comment;
    private Date date;
    private String blogId;
    private int pid;
    private int commentLike;
    private User user;
    private int isUserLike;

    public int getIsUserLike() {
        return isUserLike;
    }

    public void setIsUserLike(int isUserLike) {
        this.isUserLike = isUserLike;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCommentLike() {
        return commentLike;
    }

    public void setCommentLike(int commentLike) {
        this.commentLike = commentLike;
    }
}
