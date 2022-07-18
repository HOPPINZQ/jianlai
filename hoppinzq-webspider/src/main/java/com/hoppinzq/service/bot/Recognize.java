package com.hoppinzq.service.bot;


import com.hoppinzq.service.html.Attribute;
import com.hoppinzq.service.html.AttributeList;
import com.hoppinzq.service.html.HTMLForm;
import com.hoppinzq.service.html.HTMLPage;
import com.hoppinzq.service.log.Log;

import javax.swing.text.BadLocationException;
import java.io.IOException;

/**
 * @author: zq
 */
public abstract class Recognize {
    protected CatBot _controller;

    public abstract boolean isRecognizable(HTMLPage htmlPage);

    protected abstract boolean internalPerform(HTMLPage htmlPage) throws IOException, BadLocationException;

    public abstract boolean isRecognized();

    public Recognize(CatBot catBot) {
        this._controller = catBot;
    }

    public boolean perform(HTMLPage htmlPage) {
        try {
            return !this.isRecognizable(htmlPage) ? false : this.internalPerform(htmlPage);
        } catch (IOException ioException) {
            Log.logException("CatBot IO exception during perform:", ioException);
            return false;
        } catch (BadLocationException badLocationException) {
            Log.logException("CatBot HTML Parse exception during perform:", badLocationException);
            return false;
        }
    }

    public HTMLForm.FormElement has(HTMLForm htmlForm, String s) {
        if (htmlForm == null) {
            return null;
        } else {
            for(int i = 0; i < htmlForm.length(); ++i) {
                HTMLForm.FormElement formElement = (HTMLForm.FormElement)htmlForm.get(i);
                if (formElement.getType().equalsIgnoreCase(s)) {
                    return formElement;
                }
            }

            return null;
        }
    }

    public String findOption(AttributeList a, String opt) {
        if (a == null) {
            return null;
        } else {
            opt = opt.toUpperCase();

            for(int var3 = 0; var3 < a.length(); ++var3) {
                Attribute var4 = a.get(var3);
                if (var4.getName().toUpperCase().indexOf(opt) != -1) {
                    return var4.getValue();
                }
            }

            return null;
        }
    }

    public String findPrompt(HTMLForm htmlForm, String pr) {
        pr = pr.toUpperCase();

        for(int i = 0; i < htmlForm.length(); ++i) {
            HTMLForm.FormElement formElement = (HTMLForm.FormElement)htmlForm.get(i);
            String name = formElement.getName();
            if (name != null && name.toUpperCase().indexOf(pr) != -1) {
                return formElement.getName();
            }
        }

        return null;
    }
}
