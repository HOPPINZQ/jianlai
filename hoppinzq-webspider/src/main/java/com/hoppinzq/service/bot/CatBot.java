package com.hoppinzq.service.bot;


import com.hoppinzq.service.html.HTMLPage;
import com.hoppinzq.service.html.HTTP;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author: zq
 */
public class CatBot {
    protected String _uid = "";
    protected String _pwd = "";
    protected String _country = "";
    protected String _url = "";
    protected HTTP _http;
    protected Vector _recognizers = new Vector();
    protected Recognize _primeRecognizer;

    public CatBot(HTTP http) {
        this._http = http;
    }

    public void setUID(String uid) {
        this._uid = uid;
    }

    public String getUID() {
        return this._uid;
    }

    public void setCountry(String country) {
        this._country = country;
    }

    public String getCountry() {
        return this._country;
    }

    public void setPWD(String pwd) {
        this._pwd = pwd;
    }

    public String getPWD() {
        return this._pwd;
    }

    public void setURL(String url) {
        this._url = url;
    }

    public HTTP getHTTP() {
        return this._http;
    }

    public Vector getRecognizers() {
        return this._recognizers;
    }

    protected HTMLPage standardRecognition() throws IOException, BadLocationException {
        HTMLPage htmlPage = new HTMLPage(this._http);
        htmlPage.open(this._url, (HTMLEditorKit.ParserCallback)null);

        boolean isE;
        do {
            isE = false;
            if (this._primeRecognizer.perform(htmlPage)) {
                return htmlPage;
            }

            Enumeration enumeration = this._recognizers.elements();

            while(enumeration.hasMoreElements()) {
                Recognize recognize = (Recognize)enumeration.nextElement();
                if (!recognize.isRecognized() && recognize.perform(htmlPage)) {
                    isE = true;
                    break;
                }
            }
        } while(isE && !this._primeRecognizer.isRecognized());

        return this._primeRecognizer.isRecognized() ? htmlPage : null;
    }
}
