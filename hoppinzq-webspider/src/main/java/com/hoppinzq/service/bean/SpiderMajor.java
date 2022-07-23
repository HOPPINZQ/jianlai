package com.hoppinzq.service.bean;

import java.util.List;

/**
 * @author: zq
 */
public class SpiderMajor {
    private long id;
    private String name;
    private String create;
    private String description;
    private String urldemo;
    private int threadNum=1;
    private List<SpiderBean> spiderBeanList;

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        if(threadNum>5){
            this.threadNum=5;
        }else{
            this.threadNum = threadNum;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrldemo() {
        return urldemo;
    }

    public void setUrldemo(String urldemo) {
        this.urldemo = urldemo;
    }

    public List<SpiderBean> getSpiderBeanList() {
        return spiderBeanList;
    }

    public void setSpiderBeanList(List<SpiderBean> spiderBeanList) {
        this.spiderBeanList = spiderBeanList;
    }
}
