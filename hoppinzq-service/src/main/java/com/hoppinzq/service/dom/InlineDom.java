package com.hoppinzq.service.dom;


/**
 * @author:ZhangQi
 * 行内元素
 **/
public abstract class InlineDom extends Dom{

    enum InlineElement{
        A,SPAN,STRONG,EM,BR,IMG,INPUT,LABEL,BUTTON,SELECT,TEXTAREA,I
    }

    static String STRONG = Strong.class.getName();

    private String fontSize;
    private String fontColor;

    public InlineDom() {
    }
}
