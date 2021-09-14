package com.hoppinzq.service.dom;

/**
 * @author:ZhangQi
 **/
public class Table extends BlockDom{
    private Thead thead;
    private Tbody tbody;

    public Table(Thead thead,Tbody tbody) {
        this.thead=thead;
        this.tbody=tbody;
    }

    public Table(Tbody tbody) {
        this.tbody=tbody;
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder("<table");
        sb.append(tagAttribute()).append(tagStyle()).append(">");
        if(thead!=null){
            sb.append(thead.toHtml());
        }
        sb.append(tbody.toHtml());
        sb.append("</table>");
        return sb.toString();
    }

    @Override
    public String toFormatHtml() {
        StringBuilder sb = new StringBuilder("<table");
        sb.append(tagStyle()).append(">\n");
        if(thead!=null){
            sb.append(thead.toFormatHtml());
        }
        sb.append(tbody.toFormatHtml());
        sb.append("</table>\n");
        return sb.toString();
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
        StringBuilder sb = new StringBuilder();
        if(thead!=null){
            sb.append(thead.text());
        }
        sb.append("\n").append(tbody.text());
        return sb.toString();
    }
}
