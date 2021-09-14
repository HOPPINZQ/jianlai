package com.hoppinzq.service.dom;

/**
 * @author:ZhangQi
 **/
public class Strong extends InlineDom{
    private Object text;

    public void addValue(String value) {
        this.text = value;
    }

    public Strong(Object text) {
        super();
        this.text = text;
    }


    public Strong() {
        super();
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder("<strong");
        sb.append(tagAttribute()).append(tagStyle()).append(">");
        sb.append(text);
        sb.append("</strong>");
        return sb.toString();
    }

    @Override
    public String toFormatHtml() {
        return this.toHtml();
    }

    @Override
    public void clearElement() {
        super.clearStyle();
    }

    @Override
    public HtmlTag style(String styleKey, String styleVal) {
        this.styleMap.put(styleKey, styleVal);
        return this;
    }

    @Override
    public String text() {
        return this.text.toString();
    }
}
