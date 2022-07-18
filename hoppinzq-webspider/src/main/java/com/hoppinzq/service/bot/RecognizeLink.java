package com.hoppinzq.service.bot;

import com.hoppinzq.service.html.HTMLPage;
import com.hoppinzq.service.html.Link;
import com.hoppinzq.service.log.Log;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author: zq
 */
public class RecognizeLink extends Recognize {
    String _search;
    String _targetHREF;

    public RecognizeLink(CatBot catBot) {
        super(catBot);
    }

    public boolean isRecognized() {
        return this._targetHREF != null;
    }

    public String getSearch() {
        return this._search;
    }

    public void setSearch(String search) {
        this._search = search.toUpperCase();
    }

    public boolean isRecognizable(HTMLPage htmlPage) {
        Enumeration enumeration = htmlPage.getLinks().elements();

        Link link;
        String alt;
        String href;
        String pro;
        do {
            do {
                if (!enumeration.hasMoreElements()) {
                    return false;
                }

                link = (Link)enumeration.nextElement();
                alt = link.getALT();
                href = link.getHREF();
                pro = link.getPrompt();
                if (pro == null) {
                    pro = "";
                }

                if (alt == null) {
                    alt = "";
                }
            } while(href == null);
        } while(alt.toUpperCase().indexOf(this._search) == -1 && pro.toUpperCase().indexOf(this._search) == -1 && href.toUpperCase().indexOf(this._search) == -1);

        this._targetHREF = link.getHREF();
        Log.log(3, "Recognized a link:" + this._search);
        return true;
    }

    protected boolean internalPerform(HTMLPage htmlPage) throws IOException, BadLocationException {
        if (this._targetHREF != null) {
            htmlPage.open(this._targetHREF, (HTMLEditorKit.ParserCallback)null);
            return true;
        } else {
            return false;
        }
    }
}
