package com.hoppinzq.service.html;


import java.util.Vector;

/**
 * @author: zq
 */
public class AttributeList extends Attribute implements Cloneable {
    protected Vector _vec = new Vector();

    public Object clone() {
        AttributeList attributeList = new AttributeList();

        for(int i = 0; i < this._vec.size(); ++i) {
            attributeList.add((Attribute)this.get(i).clone());
        }

        return attributeList;
    }

    public AttributeList() {
        super("", "");
    }

    public synchronized Attribute get(int size) {
        return size < this._vec.size() ? (Attribute)this._vec.elementAt(size) : null;
    }

    public synchronized Attribute get(String s) {
        for(int i = 0; this.get(i) != null; ++i) {
            if (this.get(i).getName().equalsIgnoreCase(s)) {
                return this.get(i);
            }
        }

        return null;
    }

    public synchronized void add(Attribute attribute) {
        this._vec.addElement(attribute);
    }

    public synchronized void clear() {
        this._vec.removeAllElements();
    }

    public synchronized boolean isEmpty() {
        return this._vec.size() <= 0;
    }

    public synchronized int length() {
        return this._vec.size();
    }

    public synchronized void set(String s1, String s2) {
        if (s1 != null) {
            if (s2 == null) {
                s2 = "";
            }

            Attribute attribute = this.get(s1);
            if (attribute == null) {
                attribute = new Attribute(s1, s2);
                this.add(attribute);
            } else {
                attribute.setValue(s2);
            }

        }
    }
}
