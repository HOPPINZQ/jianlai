package com.hoppinzq.service.html;


import com.hoppinzq.service.log.Log;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author: zq
 */
public abstract class HTTP {
    protected StringBuffer _body = new StringBuffer();
    protected StringBuffer _header = new StringBuffer();
    protected String _url;
    protected boolean _autoRedirect = true;
    protected static AttributeList _cookieStore = new AttributeList();
    protected AttributeList _clientHeaders = new AttributeList();
    protected AttributeList _serverHeaders = new AttributeList();
    protected boolean _useCookies = false;
    protected boolean _usePermCookies = false;
    protected String _response;
    protected int _timeout = 30000;
    protected String _referrer = null;
    protected String _agent = "Mozilla/4.0";
    protected String _user = "";
    protected String _password = "";
    protected int _maxBodySize = -1;

    public HTTP() {
    }

    abstract HTTP copy();

    protected abstract void lowLevelSend(String url, String str2) throws UnknownHostException, IOException;

    public void clearCookies() {
        _cookieStore.clear();
    }

    protected void addCookieHeader() {
        int i = 0;
        String str = "";
        if (_cookieStore.get(0) != null) {
            while(_cookieStore.get(i) != null) {
                if (str.length() != 0) {
                    str = str + "; ";
                }

                CookieParse cookieParse = (CookieParse)_cookieStore.get(i);
                str = str + cookieParse.toString();
                ++i;
            }

            this._clientHeaders.set("Cookie", str);
            Log.log(2, "Send cookie: " + str);
        }
    }

    protected void addAuthHeader() {
        if (this._user.length() != 0 && this._password.length() != 0) {
            String up = this._user + ":" + this._password;
            String baseup = URLUtility.base64Encode(up);
            this._clientHeaders.set("Authorization", "Basic " + baseup);
        }
    }

    public void send(String url, String ty) throws UnknownHostException, IOException {
        if (ty == null) {
            Log.log(3, "HTTP GET " + url);
        } else {
            Log.log(3, "HTTP POST " + url);
        }

        this.setURL(url);
        if (this._referrer != null) {
            this._clientHeaders.set("referrer", this._referrer.toString());
        }

        this._clientHeaders.set("Accept", "image/gif,image/x-xbitmap,image/jpeg, image/pjpeg, application/vnd.ms-excel,application/msword,application/vnd.ms-powerpoint, */*");
        this._clientHeaders.set("Accept-Language", "en-us");
        this._clientHeaders.set("User-Agent", this._agent);

        while(true) {
            if (this._useCookies) {
                this.addCookieHeader();
            }

            this.addAuthHeader();
            this.lowLevelSend(this._url, ty);
            if (this._useCookies) {
                this.parseCookies();
            }

            Attribute attribute = this._serverHeaders.get("Location");
            if (attribute == null || !this._autoRedirect) {
                this._referrer = this.getURL();
                return;
            }

            URL url1 = new URL(new URL(this._url), attribute.getValue());
            Log.log(3, "HTTP REDIRECT to " + url1.toString());
            ty = null;
            this.setURL(url1.toString());
        }
    }

    public String getBody() {
        return new String(this._body);
    }

    public String getURL() {
        return this._url;
    }

    public void setURL(String u) {
        this._url = u;
    }

    public void SetAutoRedirect(boolean r) {
        this._autoRedirect = r;
    }

    public AttributeList getClientHeaders() {
        return this._clientHeaders;
    }

    public AttributeList getServerHeaders() {
        return this._serverHeaders;
    }

    protected void parseCookies() {
        int i = 0;

        while(true) {
            Attribute attribute;
            do {
                if ((attribute = this._serverHeaders.get(i++)) == null) {
                    return;
                }
            } while(!attribute.getName().equalsIgnoreCase("set-cookie"));

            CookieParse cookieParse = new CookieParse();
            cookieParse._source = new StringBuffer(attribute.getValue());
            cookieParse.get();
            cookieParse.setName(cookieParse.get(0).getName());
            if (_cookieStore.get(cookieParse.get(0).getName()) == null && (cookieParse.get("expires") == null || cookieParse.get("expires") != null && this._usePermCookies)) {
                _cookieStore.add(cookieParse);
            }

            Log.log(2, "Got cookie: " + cookieParse.toString());
        }
    }

    public CookieParse getCookie(String c) {
        return (CookieParse)_cookieStore.get(c);
    }

    public void setUseCookies(boolean c, boolean p) {
        this._useCookies = c;
        this._usePermCookies = p;
    }

    public boolean getUseCookies() {
        return this._useCookies;
    }

    public boolean getPerminantCookies() {
        return this._usePermCookies;
    }

    protected void processResponse(String response) throws IOException {
        int i = 0;
        this._response = response;
        int index = this._response.indexOf(32);
        if (index != -1) {
            int ri = this._response.indexOf(32, index + 1);
            if (ri != -1) {
                try {
                    i = Integer.parseInt(this._response.substring(index + 1, ri));
                } catch (Exception e) {
                }

                if (i >= 400 && i <= 599) {
                    throw new IOException(this._response);
                }
            }
        }

        Log.log(2, "Response: " + response);
    }

    protected void parseHeaders() throws IOException {
        boolean isC = true;
        this._serverHeaders.clear();
        String s = "";
        String header = new String(this._header);

        for(int i = 0; i < header.length(); ++i) {
            if (header.charAt(i) == '\n') {
                if (s.length() == 0) {
                    return;
                }

                if (isC) {
                    isC = false;
                    this.processResponse(s);
                } else {
                    int index = s.indexOf(58);
                    if (index != -1) {
                        String s1 = s.substring(index + 1).trim();
                        s = s.substring(0, index);
                        Attribute attribute = new Attribute(s, s1);
                        this._serverHeaders.add(attribute);
                        Log.log(2, "Sever Header:" + s + "=" + s1);
                    }
                }

                s = "";
            } else if (header.charAt(i) != '\r') {
                s = s + header.charAt(i);
            }
        }

    }

    public void setTimeout(int timeout) {
        this._timeout = timeout;
    }

    public int getTimeout() {
        return this._timeout;
    }

    public void setMaxBody(int maxBody) {
        this._maxBodySize = maxBody;
    }

    public int getMaxBody() {
        return this._maxBodySize;
    }

    public String getAgent() {
        return this._agent;
    }

    public void setAgent(String agent) {
        this._agent = agent;
    }

    public void setUser(String user) {
        this._user = user;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getUser() {
        return this._user;
    }

    public String getPassword() {
        return this._password;
    }

    public String getReferrer() {
        return this._referrer;
    }

    public void setReferrer(String referrer) {
        this._referrer = referrer;
    }

    public AttributeList getCookies() {
        return _cookieStore;
    }
}

