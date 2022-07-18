package com.hoppinzq.service.html;


/**
 * @author: zq
 */
public class HTMLParser extends Parse {
    public HTMLParser() {
    }

    public HTMLTag getTag() {
        HTMLTag htmlTag = new HTMLTag();
        htmlTag.setName(super._tag);

        for(int i = 0; i < super._vec.size(); ++i) {
            htmlTag.add((Attribute)this.get(i).clone());
        }

        return htmlTag;
    }

    public String buildTag() {
        String tag = "<";
        tag = tag + super._tag;

        for(int i = 0; this.get(i) != null; ++i) {
            tag = tag + " ";
            if (this.get(i).getValue() == null) {
                if (this.get(i).getDelim() != 0) {
                    tag = tag + this.get(i).getDelim();
                }

                tag = tag + this.get(i).getName();
                if (this.get(i).getDelim() != 0) {
                    tag = tag + this.get(i).getDelim();
                }
            } else {
                tag = tag + this.get(i).getName();
                if (this.get(i).getValue() != null) {
                    tag = tag + "=";
                    if (this.get(i).getDelim() != 0) {
                        tag = tag + this.get(i).getDelim();
                    }

                    tag = tag + this.get(i).getValue();
                    if (this.get(i).getDelim() != 0) {
                        tag = tag + this.get(i).getDelim();
                    }
                }
            }
        }

        tag = tag + ">";
        return tag;
    }

    protected void parseTag() {
        ++super._idx;
        super._tag = "";
        this.clear();
        if (super._source.charAt(super._idx) == '!' && super._source.charAt(super._idx + 1) == '-' && super._source.charAt(super._idx + 2) == '-') {
            for(; !this.eof() && (super._source.charAt(super._idx) != '-' || super._source.charAt(super._idx + 1) != '-' || super._source.charAt(super._idx + 2) != '>'); ++super._idx) {
                if (super._source.charAt(super._idx) != '\r') {
                    super._tag = super._tag + super._source.charAt(super._idx);
                }
            }

            super._tag = super._tag + "--";
            super._idx += 3;
            super._parseDelim = 0;
        } else {
            while(!this.eof() && !Parse.isWhiteSpace(super._source.charAt(super._idx)) && super._source.charAt(super._idx) != '>') {
                super._tag = super._tag + super._source.charAt(super._idx);
                ++super._idx;
            }

            this.eatWhiteSpace();

            while(super._source.charAt(super._idx) != '>') {
                super._parseName = "";
                super._parseValue = "";
                super._parseDelim = 0;
                this.parseAttributeName();
                if (super._source.charAt(super._idx) == '>') {
                    this.addAttribute();
                    break;
                }

                this.parseAttributeValue();
                this.addAttribute();
            }

            ++super._idx;
        }
    }

    public char get() {
        if (super._source.charAt(super._idx) != '<') {
            return super._source.charAt(super._idx++);
        } else {
            char  c = Character.toUpperCase(super._source.charAt(super._idx + 1));
            if ((c < 'A' || c > 'Z') && c != '!' && c != '/') {
                return super._source.charAt(super._idx++);
            } else {
                this.parseTag();
                return '\u0000';
            }
        }
    }
}
