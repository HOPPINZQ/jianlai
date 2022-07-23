package com.hoppinzq.service.bean;

import com.hoppinzq.service.util.EmojiConvert;

/**
 * @author: zq
 */
public class SpiderLink {
    private int id;
    private String title;
    private String link;
    private int isIndex=0;

    public SpiderLink(int id, String title, String link, int isIndex) {
        this.id = id;
        try{
            this.title = EmojiConvert.emojiConvertToUtf(title);
        }catch (Exception ex){
            this.title = "";
        }
        this.link = link;
        this.isIndex = isIndex;
    }

    public SpiderLink(String title, String link) {
        try{
            this.title = EmojiConvert.emojiConvertToUtf(title);
        }catch (Exception ex){
            this.title = "";
        }
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        try{
            this.title = EmojiConvert.emojiConvertToUtf(title);
        }catch (Exception ex){
            this.title = "";
        }

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(int isIndex) {
        this.isIndex = isIndex;
    }


}

