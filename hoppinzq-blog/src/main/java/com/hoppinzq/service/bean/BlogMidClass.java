package com.hoppinzq.service.bean;

/**
 * @author: zq
 */
public class BlogMidClass {

    private String blogId;
    private String classId;

    public BlogMidClass(String blogId, String classId) {
        this.blogId = blogId;
        this.classId = classId;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
