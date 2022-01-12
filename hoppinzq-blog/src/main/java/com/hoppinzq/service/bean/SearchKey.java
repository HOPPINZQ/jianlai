package com.hoppinzq.service.bean;

import com.hoppinzq.service.util.EncryptUtil;

/**
 * @author: zq
 */
public class SearchKey {

    private String searchFull;
    private String searchFc;
    private String author;
    private int searchNum;

    public SearchKey() {
    }

    public SearchKey(String searchFull, String searchFc, String author) {
        this.searchFull = searchFull;
        this.searchFc = searchFc;
        this.author = author;
    }

    public int getSearchNum() {
        return searchNum;
    }

    public void setSearchNum(int searchNum) {
        this.searchNum = searchNum;
    }

    public String getSearchFull() {
        return searchFull;
    }

    public void setSearchFull(String searchFull) {
        this.searchFull = searchFull;
    }

    public String getSearchFc() {
        return searchFc;
    }

    public void setSearchFc(String searchFc) {
        this.searchFc = searchFc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
