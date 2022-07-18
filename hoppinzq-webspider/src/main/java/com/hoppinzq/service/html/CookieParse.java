package com.hoppinzq.service.html;


/**
 * @author: zq
 */
public class CookieParse extends Parse {
    public CookieParse() {
    }

    public void parseAttributeValue() {
        this.eatWhiteSpace();
        if (super._source.charAt(super._idx) == '=') {
            ++super._idx;
            this.eatWhiteSpace();
            if (super._source.charAt(super._idx) != '\'' && super._source.charAt(super._idx) != '"') {
                while(!this.eof() && super._source.charAt(super._idx) != ';') {
                    super._parseValue = super._parseValue + super._source.charAt(super._idx);
                    ++super._idx;
                }
            } else {
                super._parseDelim = super._source.charAt(super._idx);
                ++super._idx;

                while(super._source.charAt(super._idx) != super._parseDelim) {
                    super._parseValue = super._parseValue + super._source.charAt(super._idx);
                    ++super._idx;
                }

                ++super._idx;
            }

            this.eatWhiteSpace();
        }

    }

    public boolean get() {
        label22:
        while(true) {
            if (!this.eof()) {
                super._parseName = "";
                super._parseValue = "";
                this.parseAttributeName();
                if (super._source.charAt(super._idx) != ';') {
                    this.parseAttributeValue();
                    this.addAttribute();
                    this.eatWhiteSpace();

                    while(true) {
                        if (this.eof() || super._source.charAt(super._idx++) == ';') {
                            continue label22;
                        }
                    }
                }

                this.addAttribute();
            }

            ++super._idx;
            return false;
        }
    }

    public String toString() {
        String name = this.get(0).getName();
        name = name + "=";
        name = name + this.get(0).getValue();
        return name;
    }
}
