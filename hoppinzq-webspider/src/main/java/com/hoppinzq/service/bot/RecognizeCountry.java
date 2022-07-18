package com.hoppinzq.service.bot;

import com.hoppinzq.service.html.HTMLForm;
import com.hoppinzq.service.html.HTMLPage;
import com.hoppinzq.service.log.Log;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author: zq
 */
public class RecognizeCountry extends Recognize {
    protected HTMLForm _targetForm = null;
    protected HTMLForm.FormElement _targetElement = null;

    public RecognizeCountry(CatBot catBot) {
        super(catBot);
    }

    public boolean isRecognized() {
        return this._targetForm != null;
    }

    public boolean isRecognizable(HTMLPage htmlPage) {
        if (htmlPage.getForms() == null) {
            return false;
        } else {
            Vector vector = htmlPage.getForms();
            Enumeration enumeration = vector.elements();

            HTMLForm htmlForm;
            HTMLForm.FormElement formElement;
            do {
                do {
                    if (!enumeration.hasMoreElements()) {
                        return false;
                    }

                    htmlForm = (HTMLForm)enumeration.nextElement();
                    formElement = this.has(htmlForm, "select");
                } while(formElement == null);
            } while(this.findOption(formElement.getOptions(), "france") == null && this.findOption(formElement.getOptions(), "canada") == null && this.findOption(formElement.getOptions(), "japan") == null && this.findOption(formElement.getOptions(), "egypt") == null);

            this._targetForm = htmlForm;
            this._targetElement = formElement;
            Log.log(3, "Recognized a country page");
            return true;
        }
    }

    protected boolean internalPerform(HTMLPage htmlPage) throws IOException {
        if (this._targetForm == null) {
            return false;
        } else {
            String option = this.findOption(this._targetElement.getOptions(), super._controller.getCountry());
            this._targetForm.set(this._targetElement.getName(), option);
            htmlPage.post(this._targetForm);
            return true;
        }
    }
}
