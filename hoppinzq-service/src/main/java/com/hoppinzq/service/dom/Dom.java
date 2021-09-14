package com.hoppinzq.service.dom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:ZhangQi dom基类，包含所有dom常见的共有的属性
 * 因为class是java的保留字，所以都改为domXxx
 **/
public class Dom implements HtmlTag {

    private String domId;
    private String[] domClass;
    private List<DomData> domDataList;
    private Boolean domIsHidden = Boolean.FALSE;
    private String domTitle;

    Map<String, Object> styleMap = new HashMap<String, Object>();


    @Override
    public String tagAttribute() {
        StringBuilder sb = new StringBuilder();
        if (domId != null) {
            sb.append(" id='").append(domId).append("'");
        }
        if (domClass != null && domClass.length > 0) {
            sb.append(" class='");
            for (int i = 0; i < domClass.length; i++) {
                sb.append(domClass[i]);
                if (i != domClass.length - 1) {
                    sb.append(" ");
                }
            }
            sb.append("'");
        }
        if (domDataList != null && domDataList.size() > 0) {
            for (DomData domData : domDataList) {
                sb.append(" ").append(domData.getDomDataKey()).append("='")
                        .append(domData.getDomDataValue()).append("'");
            }
        }
        if (domTitle != null) {
            sb.append(" title='").append(domTitle).append("'");
        }
        if (domIsHidden) {
            sb.append(" hidden");
        }
        return sb.toString();
    }

    public String tagStyle() {
        if (styleMap.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(" style='");
            for (String styleKey : styleMap.keySet()) {
                sb.append(styleKey + ":" + styleMap.get(styleKey) + ";");
            }
            sb.append("'");
            return sb.toString();
        }
        return "";
    }

    /**
     * 把一个字符串的第一个字母大写
     * @param fildeName
     * @return
     */
    private static String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    @Override
    public void addStyle(String key, String val) {
        styleMap.put(key, val);
    }

    @Override
    public void addStyle(Style style, String val) {
        styleMap.put(style.getStyleKey(), val);
    }


    @Override
    public void clear() {
        clearStyle();
        clearElement();
    }

    @Override
    public void clearStyle() {
        styleMap.clear();
    }

    @Override
    public String toHtml() {
        return null;
    }

    @Override
    public String toFormatHtml() {
        return null;
    }


    @Override
    public void clearElement() {

    }

    @Override
    public HtmlTag style(String styleKey, String styleVal) {
        return null;
    }


    @Override
    public String text() {
        return null;
    }


    public String getDomId() {
        return domId;
    }

    public void setDomId(String domId) {
        this.domId = domId;
    }

    public String[] getDomClass() {
        return domClass;
    }

    public void setDomClass(String[] domClass) {
        this.domClass = domClass;
    }

    public List<DomData> getDomDataList() {
        return domDataList;
    }

    public void setDomDataList(List<DomData> domDataList) {
        this.domDataList = domDataList;
    }


    public Boolean getDomIsHidden() {
        return domIsHidden;
    }

    public void setDomIsHidden(Boolean domIsHidden) {
        this.domIsHidden = domIsHidden;
    }

    public String getDomTitle() {
        return domTitle;
    }

    public void setDomTitle(String domTitle) {
        this.domTitle = domTitle;
    }


}
