package com.hoppinzq.service.html;


import java.net.URLEncoder;

/**
 * @author: zq
 */
public class HTMLForm extends AttributeList {
    protected String _method;
    protected String _action;

    public HTMLForm(String method, String action) {
        this._method = method;
        this._action = action;
    }

    public String getAction() {
        return this._action;
    }

    public String getMethod() {
        return this._method;
    }

    public void addInput(String name, String value, String type, String prompt, AttributeList attributeList) {
        HTMLForm.FormElement var6 = new HTMLForm.FormElement();
        var6.setName(name);
        var6.setValue(value);
        var6.setType(type.toUpperCase());
        var6.setOptions(attributeList);
        var6.setPrompt(prompt);
        this.add(var6);
    }

    public String toString() {
        String s = "";

        for(int i = 0; this.get(i) != null; ++i) {
            Attribute var3 = this.get(i);
            if (s.length() > 0) {
                s = s + "&";
            }

            s = s + var3.getName();
            s = s + "=";
            if (var3.getValue() != null) {
                s = s + URLEncoder.encode(var3.getValue());
            }
        }

        return s;
    }

    public class FormElement extends Attribute {
        protected String _type;
        protected AttributeList _options;
        protected String _prompt;

        public FormElement() {
        }

        public void setOptions(AttributeList attributeList) {
            this._options = attributeList;
        }

        public AttributeList getOptions() {
            return this._options;
        }

        public void setType(String type) {
            this._type = type;
        }

        public String getType() {
            return this._type;
        }

        public String getPrompt() {
            return this._prompt;
        }

        public void setPrompt(String prompt) {
            this._prompt = prompt;
        }
    }
}
