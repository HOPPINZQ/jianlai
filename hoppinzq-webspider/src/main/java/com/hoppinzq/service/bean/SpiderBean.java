package com.hoppinzq.service.bean;

/**
 * @author: zq
 */
public class SpiderBean {

    private String key;
    private String description;
    private String xpath;
    private String selector;
    private String attr;
    private Boolean links=false;
    private Boolean addLinks=false;
    private String regex;
    private Boolean isAll=false;
    private String xpathFunction="text(0)";

    public SpiderBean(){}

    public String getXpathFunction() {
        return xpathFunction;
    }

    public void setXpathFunction(String xpathFunction) {
        this.xpathFunction = xpathFunction;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getAttr() {
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
