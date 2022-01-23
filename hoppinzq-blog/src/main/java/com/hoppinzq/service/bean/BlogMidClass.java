package com.hoppinzq.service.bean;

/**
 * @author: zq
 */
public class BlogMidClass {

    private long blogId;
    private long classId;
    private long authId;

    public BlogMidClass(long blogId, long classId,long authId) {
        this.blogId = blogId;
        this.classId = classId;
    }

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long blogId) {
        this.blogId = blogId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long authId) {
        this.authId = authId;
    }
}
