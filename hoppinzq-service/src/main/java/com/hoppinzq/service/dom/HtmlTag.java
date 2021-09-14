package com.hoppinzq.service.dom;


/**
 * @author:ZhangQi
 */
public interface HtmlTag {

    /**
     * @return  输出html代码
     */
    String toHtml();

    /**
     * @return  输出格式化的html代码
     */
    String toFormatHtml();

    /**
     * 清除标签的所有属性和元素
     */
    void clear();

    /**
     * 清除标签的所有css样式
     */
    void clearStyle();


    /**
     * 清除元素的所有子元素
     */
    void clearElement();


    /**
     * html标签添加css样式并返回该标签对象
     * @param styleKey 样式key
     * @param styleVal 样式值
     * @return IHtmlTag 自己
     */
    HtmlTag style(String styleKey, String styleVal);


    /**
     * 给html标签设置css样式
     * @param styleKey 样式key
     * @param styleVal 样式值
     */
    void addStyle(String styleKey, String styleVal);

    /**
     * 通过枚举设置样式
     * @param style
     * @param val
     */
    void addStyle(Style style, String val);

    /**
     * 标签属性设置
     * @return
     */
    String tagAttribute();

    /**
     * @return 返回标签的文本内容
     */
    String text();

}