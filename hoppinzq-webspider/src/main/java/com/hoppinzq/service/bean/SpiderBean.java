package com.hoppinzq.service.bean;

import java.util.Map;

/**
 * @author: zq
 */
public class SpiderBean {

    private int id;
    private long mid;
    private String key;
    private String description;
    private String xpath;
    private String selector;
    private String attr;
    private Boolean links=false;
    private Boolean addLinks=false;
    private String regex;
    private Boolean isAll=false;
    private String xpathFunction;

    public SpiderBean(){}

    public SpiderBean(long mid, Map map) {
        this.mid = mid;
        if(map.containsKey("key")){
            this.key=map.get("key").toString();
        }
        if(map.containsKey("description")){
            this.description=map.get("description").toString();
        }
        if(map.containsKey("xpath")){
            this.xpath=map.get("xpath").toString();
        }
        if(map.containsKey("selector")){
            this.selector=map.get("selector").toString();
        }
        if(map.containsKey("attr")){
            this.attr=map.get("attr").toString();
        }
        if(map.containsKey("regex")){
            this.regex=map.get("regex").toString();
        }
        if(map.containsKey("xpathFunction")){
            this.xpathFunction=map.get("xpathFunction").toString();
        }
        if(map.containsKey("links")){
            if("true".equals(map.get("links").toString())){
                this.links=true;
            }

        }if(map.containsKey("addLinks")){
            if("true".equals(map.get("addLinks").toString())){
                this.addLinks=true;
            }
        }if(map.containsKey("isAll")){
            if("true".equals(map.get("isAll").toString())){
                this.isAll=true;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public String getXpathFunction() {
        if(xpathFunction!=null)
        return xpathFunction.trim();
        return xpathFunction;
    }

    public void setXpathFunction(String xpathFunction) {
        this.xpathFunction = xpathFunction;
    }

    public String getKey() {
        if(key!=null)
        return key.trim();
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        if(description!=null)
        return description.trim();
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getXpath() {
        if(xpath!=null)
        return xpath.trim();
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getSelector() {
        if(selector!=null)
        return selector.trim();
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getAttr() {
        if(attr!=null)
        return attr.trim();
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public Boolean getLinks() {
        return links;
    }

    public void setLinks(Boolean links) {
        this.links = links;
    }

    public Boolean getAddLinks() {
        return addLinks;
    }

    public void setAddLinks(Boolean addLinks) {
        this.addLinks = addLinks;
    }

    public String getRegex() {
        if(regex!=null)
        return regex.trim();
        return regex;

    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Boolean getAll() {
        return isAll;
    }

    public void setAll(Boolean all) {
        isAll = all;
    }
}
