package com.hoppinzq.service.bean;

/**
 * @author: zq
 */
public class BlogClass {
    private long id;
    private long parent_id;
    private String name;
    private String author;

    public BlogClass(long id, long parent_id, String name, String author) {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
