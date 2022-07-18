package com.hoppinzq.service.html;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * @author: zq
 */
public class HTMLPage {
    protected Vector _images = new Vector();
    protected Vector _links = new Vector();
    protected Vector _forms = new Vector();
    protected HTTP _http;
    protected String _base;

    public HTMLPage(HTTP http) {
        this._http = http;
    }

    public void open(String url, HTMLEditorKit.ParserCallback callback) throws IOException, BadLocationException {
        this._http.send(url, (String)null);
        this._base = url;
        this.processPage(callback);
    }

    protected void processPage(HTMLEditorKit.ParserCallback callback) throws IOException {
        StringReader stringReader = new StringReader(this._http.getBody());
        HTMLEditorKit.Parser parser = (new HTMLParse()).getParser();
        if (callback == null) {
            HTMLPage.Parser parser1 = new HTMLPage.Parser();
            parser.parse(stringReader, parser1, true);
        } else {
            parser.parse(stringReader, callback, false);
        }

    }

    public HTTP getHTTP() {
        return this._http;
    }

    public Vector getLinks() {
        return this._links;
    }

    public Vector getImages() {
        return this._images;
    }

    public Vector getForms() {
        return this._forms;
    }

    public void post(HTMLForm htmlForm) throws IOException {
        this._http.getClientHeaders().set("Content-Type", "application/x-www-form-urlencoded");
        this._http.send(htmlForm.getAction(), htmlForm.toString());
        this.processPage((HTMLEditorKit.ParserCallback)null);
    }

    public String getURL() {
        return this._http.getURL();
    }

    protected void addImage(String spec) {
        spec = URLUtility.resolveBase(this._base, spec);

        for(int i = 0; i < this._images.size(); ++i) {
            String s = (String)this._images.elementAt(i);
            if (s.equalsIgnoreCase(spec)) {
                return;
            }
        }

        this._images.addElement(spec);
    }

    protected class Parser extends HTMLEditorKit.ParserCallback {
        protected HTMLForm _tempForm;
        protected AttributeList _tempOptions;
        protected Attribute _tempElement = new Attribute();
        protected String _tempPrompt = "";
        protected Link _tempLink;

        protected Parser() {
        }

        public void handleComment(char[] chars, int i) {
        }

        public void handleEndTag(HTML.Tag tag, int i) {
            if (tag == HTML.Tag.OPTION) {
                if (this._tempElement != null) {
                    this._tempElement.setName(this._tempPrompt);
                    this._tempOptions.add(this._tempElement);
                    this._tempPrompt = "";
                }

                this._tempElement = null;
            } else if (tag == HTML.Tag.FORM) {
                if (this._tempForm != null) {
                    HTMLPage.this._forms.addElement(this._tempForm);
                }

                this._tempPrompt = "";
            } else if (tag == HTML.Tag.A) {
                if (this._tempLink != null) {
                    this._tempLink.setPrompt(this._tempPrompt);
                }

                this._tempPrompt = "";
            }

        }

        public void handleError(String u, int i) {
        }

        public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet set, int i) {
            this.handleStartTag(tag, set, i);
        }

        public void handleStartTag(HTML.Tag t, MutableAttributeSet s, int i) {
            String type = "";
            String href = (String)s.getAttribute(javax.swing.text.html.HTML.Attribute.HREF);
            String attr;
            if (href != null && t != HTML.Tag.BASE) {
                attr = (String)s.getAttribute(javax.swing.text.html.HTML.Attribute.ALT);
                Link link = new Link(attr, URLUtility.resolveBase(HTMLPage.this._base, href), (String)null);
                HTMLPage.this._links.addElement(this._tempLink = link);
            } else if (t == HTML.Tag.OPTION) {
                this._tempElement = new Attribute();
                this._tempElement.setName("");
                this._tempElement.setValue((String)s.getAttribute(javax.swing.text.html.HTML.Attribute.VALUE));
            } else if (t == HTML.Tag.SELECT) {
                if (this._tempForm == null) {
                    return;
                }

                this._tempOptions = new AttributeList();
                this._tempForm.addInput((String)s.getAttribute(javax.swing.text.html.HTML.Attribute.NAME), (String)null, "select", this._tempPrompt, this._tempOptions);
                this._tempPrompt = "";
            } else if (t == HTML.Tag.TEXTAREA) {
                if (this._tempForm == null) {
                    return;
                }

                this._tempForm.addInput((String)s.getAttribute(javax.swing.text.html.HTML.Attribute.NAME), (String)null, "textarea", this._tempPrompt, (AttributeList)null);
                this._tempPrompt = "";
            } else if (t == HTML.Tag.FORM) {
                if (this._tempForm != null) {
                    HTMLPage.this._forms.addElement(this._tempForm);
                }

                attr = (String)s.getAttribute(javax.swing.text.html.HTML.Attribute.ACTION);
                if (attr != null) {
                    try {
                        URL url = new URL(new URL(HTMLPage.this._http.getURL()), attr);
                        attr = url.toString();
                    } catch (MalformedURLException e) {
                        attr = null;
                    }
                }

                this._tempForm = new HTMLForm((String)s.getAttribute(javax.swing.text.html.HTML.Attribute.METHOD), attr);
                this._tempPrompt = "";
            } else if (t == HTML.Tag.INPUT) {
                if (this._tempForm == null) {
                    return;
                }

                if (t != HTML.Tag.INPUT) {
                    type = (String)s.getAttribute(javax.swing.text.html.HTML.Attribute.TYPE);
                    if (type == null) {
                        return;
                    }
                } else {
                    type = "select";
                }

                if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("edit") || type.equalsIgnoreCase("password") || type.equalsIgnoreCase("select") || type.equalsIgnoreCase("hidden")) {
                    this._tempForm.addInput((String)s.getAttribute(javax.swing.text.html.HTML.Attribute.NAME), (String)s.getAttribute(javax.swing.text.html.HTML.Attribute.VALUE), type, this._tempPrompt, (AttributeList)null);
                    this._tempOptions = new AttributeList();
                }
            } else if (t == HTML.Tag.BASE) {
                href = (String)s.getAttribute(javax.swing.text.html.HTML.Attribute.HREF);
                if (href != null) {
                    HTMLPage.this._base = href;
                }
            } else if (t == HTML.Tag.IMG) {
                attr = (String)s.getAttribute(javax.swing.text.html.HTML.Attribute.SRC);
                if (attr != null) {
                    HTMLPage.this.addImage(attr);
                }
            }

        }

        public void handleText(char[] chars, int i) {
            this._tempPrompt = this._tempPrompt + new String(chars) + " ";
        }
    }
}
