package com.hoppinzq.service.html;


/**
 * @author: zq
 */
public class Parse extends AttributeList {
    public StringBuffer _source;
    protected int _idx;
    protected char _parseDelim;
    protected String _parseName;
    protected String _parseValue;
    public String _tag;

    Parse() {
    }

    public static boolean isWhiteSpace(char c) {
        return "\t\n\r ".indexOf(c) != -1;
    }

    public void eatWhiteSpace() {
        while(!this.eof()) {
            if (!isWhiteSpace(this._source.charAt(this._idx))) {
                return;
            }

            ++this._idx;
        }

    }

    public boolean eof() {
        return this._idx >= this._source.length();
    }

    public void parseAttributeName() {
        this.eatWhiteSpace();
        if (this._source.charAt(this._idx) != '\'' && this._source.charAt(this._idx) != '"') {
            while(!this.eof() && !isWhiteSpace(this._source.charAt(this._idx)) && this._source.charAt(this._idx) != '=' && this._source.charAt(this._idx) != '>') {
                this._parseName = this._parseName + this._source.charAt(this._idx);
                ++this._idx;
            }
        } else {
            this._parseDelim = this._source.charAt(this._idx);
            ++this._idx;

            while(this._source.charAt(this._idx) != this._parseDelim) {
                this._parseName = this._parseName + this._source.charAt(this._idx);
                ++this._idx;
            }

            ++this._idx;
        }

        this.eatWhiteSpace();
    }

    public void parseAttributeValue() {
        if (this._parseDelim == 0) {
            if (this._source.charAt(this._idx) == '=') {
                ++this._idx;
                this.eatWhiteSpace();
                if (this._source.charAt(this._idx) != '\'' && this._source.charAt(this._idx) != '"') {
                    while(!this.eof() && !isWhiteSpace(this._source.charAt(this._idx)) && this._source.charAt(this._idx) != '>') {
                        this._parseValue = this._parseValue + this._source.charAt(this._idx);
                        ++this._idx;
                    }
                } else {
                    this._parseDelim = this._source.charAt(this._idx);
                    ++this._idx;

                    while(this._source.charAt(this._idx) != this._parseDelim) {
                        this._parseValue = this._parseValue + this._source.charAt(this._idx);
                        ++this._idx;
                    }

                    ++this._idx;
                }

                this.eatWhiteSpace();
            }

        }
    }

    void addAttribute() {
        Attribute attribute = new Attribute(this._parseName, this._parseValue, this._parseDelim);
        this.add(attribute);
    }

    String getParseName() {
        return this._parseName;
    }

    void setParseName(String name) {
        this._parseName = name;
    }

    String getParseValue() {
        return this._parseValue;
    }

    void setParseValue(String value) {
        this._parseValue = value;
    }

    char getParseDelim() {
        return this._parseDelim;
    }

    void setParseDelim(char delim) {
        this._parseDelim = delim;
    }
}

