package com.hoppinzq.service.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:ZhangQi
 */
public class Tr extends BlockDom {

    private List<Td> tdList = new ArrayList<Td>();

    private String align;


    public Tr(Object... tdTexts) {
        for (Object obj : tdTexts) {
            if (obj instanceof Td) {
                tdList.add((Td) obj);
            } else {
                tdList.add(new Td(obj));
            }
        }
    }

    public Tr alignCenter() {
        this.align = "center";
        return this;
    }

    public Tr alignLeft() {
        this.align = "left";
        return this;
    }

    public Tr alignRight() {
        this.align = "right";
        return this;
    }

    public Tr() {
        super();
    }

    public Tr append(Td td) {
        tdList.add(td);
        return this;
    }

    public void add(Td td) {
        tdList.add(td);
    }

    public void add(Td... tds) {
        for (Td td : tds) {
            tdList.add(td);
        }
    }

    public Tr addAll(Tr tr) {
        if (tr != null) {
            for (Td td : tr.allTds()) {
                add(td);
            }
        }
        return this;
    }

    public Tr style(String key, String val) {
        this.styleMap.put(key, val);
        return this;
    }

    /**
     *
     * @return tr的html代码
     */
    public String toHtml() {
        StringBuilder sb = new StringBuilder("<tr");
        if (align != null) {
            sb.append(" align='" + align + "'");
        }
        sb.append(tagAttribute());
        sb.append(tagStyle()).append(">");
        for (Td td : tdList) {
            sb.append(td.toHtml());
        }
        sb.append("</tr>");
        return sb.toString();
    }
    @Override
    public String toFormatHtml() {
        StringBuilder sb = new StringBuilder("<tr");
        if (align != null) {
            sb.append(" align='" + align + "'");
        }
        sb.append(tagStyle()).append(">").append("\n");
        for (Td td : tdList) {
            sb.append("\t").append(td.toFormatHtml());
        }
        sb.append("</tr>\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toHtml();
    }

    @Override
    public void clearStyle() {
        this.align = null;
        super.clearStyle();
    }


    @Override
    public void clearElement() {
        this.tdList.clear();
    }

    @Override
    public String text() {
        StringBuilder sb = new StringBuilder();
        for (Td td : tdList) {
            sb.append(td.text()).append("\t");
        }
        return sb.toString();
    }

    public List<Td> allTds() {
        return tdList;
    }

    /**
     * @return 返回td的个数
     */
    public int size() {
        return tdList.size();
    }

    /**
     * 用新的obj替换指定index的td元素
     * @param index 元素下表，从0开始
     * @param newTd 新的td
     * @return 是否替换成功
     */
    public boolean replace(int index, Object newTd) {
        try {
            this.tdList.remove(index);
        } catch (Exception e) {
            return false;
        }

        return insert(index, newTd);
    }

    /**
     * 插入新的td到指定index
     * @param index td的位置
     * @param newTd 新的td
     * @return 是否插入成功
     */
    public boolean insert(int index, Object newTd) {
        try {
            if (newTd instanceof Td) {
                this.tdList.add(index, (Td) newTd);
            } else {
                this.tdList.add(index, new Td(newTd));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}