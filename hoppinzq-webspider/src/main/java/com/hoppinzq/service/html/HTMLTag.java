package com.hoppinzq.service.html;

/**
 * @author: zq
 */
public class HTMLTag extends AttributeList implements Cloneable {
    protected String _name;

    public HTMLTag() {
    }

    public Object clone() {
        AttributeList attributeList = new AttributeList();

        for(int i = 0; i < super._vec.size(); ++i) {
            attributeList.add((Attribute)this.get(i).clone());
        }

        attributeList.setName(this._name);
        return attributeList;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public String getAttributeValue(String attr) {
        Attribute a = this.get(attr);
        return a == null ? null : a.getValue();
    }
}
