package com.hoppinzq.service.bean;

/**
 * @author: zq
 */
public class Page {
    private int pageSize;//每页多少条
    private int pageIndex=1;//第几页
    private int currentNum;

    public int getCurrentNum() {
        if(pageIndex==0){
            return -1;
        }else{
            return (pageIndex-1)*pageSize;
        }
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum=currentNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
