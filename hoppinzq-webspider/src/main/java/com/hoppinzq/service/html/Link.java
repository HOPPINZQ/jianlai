package com.hoppinzq.service.html;

/**
 * @author: zq
 */
public class Link {
    protected String _alt;
    protected String _href;
    protected String _prompt;

    public Link(String alt, String href, String prompt) {
        this._alt = alt;
        this._href = href;
        this._prompt = prompt;
    }

    public String getALT() {
        return this._alt;
    }

    public String getHREF() {
        return this._href;
    }

    public String getPrompt() {
        return this._prompt;
    }

    public String toString() {
        return this._href;
    }

    public void setPrompt(String prompt) {
        this._prompt = prompt;
    }
}

