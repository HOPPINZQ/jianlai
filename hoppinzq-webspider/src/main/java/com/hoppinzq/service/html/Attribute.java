package com.hoppinzq.service.html;

/**
 * @author: zq
 */
public class Attribute implements Cloneable {
    private String _name;
    private String _value;
    private char _delim;

    public Object clone() {
        return new Attribute(this._name, this._value, this._delim);
    }

    public Attribute(String name, String value, char delim) {
        this._name = name;
        this._value = value;
        this._delim = delim;
    }

    public Attribute() {
        this("", "", '\u0000');
    }

    public Attribute(String var1, String var2) {
        this(var1, var2, '\u0000');
    }

    public String getName() {
        return this._name;
    }

    public String getValue() {
        return this._value;
    }

    public char getDelim() {
        return this._delim;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setValue(String value) {
        this._value = value;
    }

    public void setDelim(char delim) {
        this._delim = delim;
    }
}
