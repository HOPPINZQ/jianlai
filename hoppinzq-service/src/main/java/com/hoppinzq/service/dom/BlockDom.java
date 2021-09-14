package com.hoppinzq.service.dom;


import com.hoppinzq.service.utils.StringUtils;

/**
 * @author:ZhangQi
 * 块状元素抽象类，不包括在样式display:block的标签
 **/
public abstract class BlockDom extends Dom{

    enum BlockElement{
        P,DIV,FORM,UL,LI,OL,DL,HR,TABLE,H1,H2,H3,H4,H5,H6,TR,TH,TD,THEAD,TBODY
    }

    static String DIV = Div.class.getName();
    static String TD = Td.class.getName();
    static String TR = Tr.class.getName();
    static String TABLE = Table.class.getName();
    static String THEAD = Thead.class.getName();
    static String TBODY = Tbody.class.getName();
    static String H1 = H1.class.getName();


    private String width;
    private String height;
    private String top;
    private String bottom;
    private String left;
    private String right;
    private String margin;
    private String padding;
    private String marginTop;
    private String marginBottom;
    private String marginLeft;
    private String marginRight;
    private String paddingtTop;
    private String paddingtBottom;
    private String paddingtLeft;
    private String paddingtRight;
    public BlockDom() {
    }


    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
        styleMap.put("width",width);
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
        styleMap.put("height",height);
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
        styleMap.put("top",top);
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
        styleMap.put("bottom",bottom);
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
        styleMap.put("left",left);
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
        styleMap.put("right",right);
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String[] margin) {
        this.margin = StringUtils.join(margin);
        styleMap.put("margin", this.margin);
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String[] padding) {
        this.padding = StringUtils.join(padding);
        styleMap.put("padding",this.padding);
    }

    public String getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(String marginTop) {
        this.marginTop = marginTop;
        styleMap.put("margin-top",marginTop);
    }

    public String getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(String marginBottom) {
        this.marginBottom = marginBottom;
        styleMap.put("margin-bottom",marginBottom);
    }

    public String getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(String marginLeft) {
        this.marginLeft = marginLeft;
        styleMap.put("margin-left",marginLeft);
    }

    public String getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(String marginRight) {
        this.marginRight = marginRight;
        styleMap.put("margin-right",marginRight);
    }

    public String getPaddingtTop() {
        return paddingtTop;
    }

    public void setPaddingtTop(String paddingtTop) {
        this.paddingtTop = paddingtTop;
        styleMap.put("padding-top",paddingtTop);
    }

    public String getPaddingtBottom() {
        return paddingtBottom;
    }

    public void setPaddingtBottom(String paddingtBottom) {
        this.paddingtBottom = paddingtBottom;
        styleMap.put("padding-bottom",paddingtBottom);
    }

    public String getPaddingtLeft() {
        return paddingtLeft;
    }

    public void setPaddingtLeft(String paddingtLeft) {
        this.paddingtLeft = paddingtLeft;
        styleMap.put("padding-left",paddingtLeft);
    }

    public String getPaddingtRight() {
        return paddingtRight;
    }

    public void setPaddingtRight(String paddingtRight) {
        this.paddingtRight = paddingtRight;
        styleMap.put("padding-right",paddingtRight);
    }
}
