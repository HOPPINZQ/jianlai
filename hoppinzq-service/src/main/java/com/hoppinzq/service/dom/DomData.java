package com.hoppinzq.service.dom;

import java.util.Map;

/**
 * @author:ZhangQi
 * dom节点上的自定义参数,可以配置多个
 * <div data-xx:xxx></div>
 **/
public class DomData {
    private String DomDataKey;
    private String DomDataValue;

    public DomData() {}
    public DomData(String domDataKey, String domDataValue) {
        DomDataKey = "data-"+domDataKey;
        DomDataValue = domDataValue;
    }

    public String getDomDataKey() {
        return DomDataKey;
    }

    public void setDomDataKey(String domDataKey) {
        DomDataKey = "data-"+domDataKey;
    }

    public String getDomDataValue() {
        return DomDataValue;
    }

    public void setDomDataValue(String domDataValue) {
        DomDataValue = domDataValue;
    }

    public void setDomDataKey(Map map) {

    }
}
