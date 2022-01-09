package com.hoppinzq.service.bean;

import java.util.Date;

/**
 * @author: zq
 */
public class CommentVo extends Page{
    private int comment_id;
    private String comment_author;
    private Date comment_date;
    private String comment_blogId;
    private int comment_pid;
    private int comment_search_type=0;//0表示按评论时间倒序,1表示按评论时间升序，2表示按喜欢数倒叙

    public int getComment_search_type() {
        return comment_search_type;
    }

    public void setComment_search_type(int comment_search_type) {
        this.comment_search_type = comment_search_type;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_author() {
        return comment_author;
    }

    public void setComment_author(String comment_author) {
        this.comment_author = comment_author;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_blogId() {
        return comment_blogId;
    }

    public void setComment_blogId(String comment_blogId) {
        this.comment_blogId = comment_blogId;
    }

    public int getComment_pid() {
        return comment_pid;
    }

    public void setComment_pid(int comment_pid) {
        this.comment_pid = comment_pid;
    }
}
