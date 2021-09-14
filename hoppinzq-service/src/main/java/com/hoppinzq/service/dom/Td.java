package com.hoppinzq.service.dom;


/**
 * @author:ZhangQi
 */
public class Td extends BlockDom {

    private Integer colspan;

    private Integer rowspan;

    private Object text;

    private String tagTitle;

    private Integer width;

    /**
     *
     * @param text td内容
     * @param rowspan 跨行
     * @param colspan 跨列
     */
    public Td(Object text, Integer rowspan, Integer colspan) {
        super();
        this.text = text;
        if (rowspan != null) {
            this.rowspan = rowspan;
        }
        if (colspan != null) {
            this.colspan = colspan;
        }
    }

    public void addValue(String value) {
        this.text = value;
    }

    /**
     * @param text td内容
     */
    public Td(Object text) {
        super();
        this.text = text;
    }


    /**
     * @param text td内容
     */
    public Td(Object text,String tagTitle) {
        super();
        this.text = text;
        this.tagTitle=tagTitle;
    }


    public Td() {
        super();
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    /**
     *
     * @param key css样式名
     * @param val css样式值
     * @return this
     */
    public Td appendStyle(String key, String val) {
        styleMap.put(key, val);
        return this;
    }

    /*
     *
     */
    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        if("th".equals(tagTitle)){
            sb.append("<th");
        }else{
            sb.append("<td");
        }
        sb.append(tagAttribute());
        sb.append(tagStyle());
        if (colspan != null) {
            sb.append(" colspan='" + colspan + "'");
        }
        if (rowspan != null) {
            sb.append(" rowspan='" + rowspan + "'");
        }
        sb.append(">");
        if (text != null) {
            sb.append(text);
        }
        if("th".equals(tagTitle)){
            sb.append("</th>");
        }else{
            sb.append("</td>");
        }
        return sb.toString();
    }

    @Override
    public String toFormatHtml() {
        StringBuilder sb = new StringBuilder(this.toHtml()).append("\n");
        return sb.toString();
    }

    /**
     *
     * @return html
     */
    @Override
    public String toString() {
        return toHtml();
    }

    /**
     *
     * @param colspan 跨N列
     * @return this
     */
    public Td colspan(Integer colspan) {
        this.colspan = colspan;
        return this;
    }

    /**
     *
     * @param rowspan 跨N行
     * @return this
     */
    public Td rowspan(Integer rowspan) {
        this.rowspan = rowspan;
        return this;
    }


    @Override
    public void clearStyle() {
        colspan = null;
        rowspan = null;
        super.clearStyle();
    }


    @Override
    public void clearElement() {
        text = null;
    }

    @Override
    public Td style(String styleKey, String styleVal) {
        styleMap.put(styleKey, styleVal);
        return this;
    }

    @Override
    public String text() {
        return text == null ? "" : text.toString();
    }

    public Integer colspan() {
        return colspan;
    }

    public Integer rowspan() {
        return rowspan;
    }

    public void width(Integer width) {
        this.width = width;
    }

    public Integer width() {
        return this.width;
    }
}