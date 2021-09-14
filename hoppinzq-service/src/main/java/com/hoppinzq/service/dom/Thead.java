package com.hoppinzq.service.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:ZhangQi
 **/
public class Thead extends BlockDom{

    private List<Tr> trList = new ArrayList<Tr>();

    public Thead(Object... trText2) {
        for (Object obj : trText2) {
            if (obj instanceof Tr) {
                trList.add((Tr) obj);
            } else {
                trList.add(new Tr(obj));
            }
        }
    }

    @Override
    public String toHtml() {
        StringBuilder sb = new StringBuilder("<thead");
        sb.append(tagAttribute()).append(tagStyle()).append(">");
        for (Tr tr : trList) {
            sb.append(tr.toHtml());
        }
        sb.append("</thead>");
        return sb.toString();
    }

    @Override
    public String toFormatHtml() {
        StringBuilder sb = new StringBuilder("<thead");
        sb.append(tagStyle()).append(">\n");
        for (Tr tr : trList) {
            sb.append(tr.toFormatHtml());
        }
        sb.append("</thead>\n");
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
        for (Tr tr : trList) {
            sb.append(tr.text()).append("\n");
        }
        return sb.toString();
    }

}
