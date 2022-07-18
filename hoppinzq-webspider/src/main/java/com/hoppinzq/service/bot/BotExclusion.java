package com.hoppinzq.service.bot;

import com.hoppinzq.service.html.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author: zq
 */
public class BotExclusion {
    protected String _robotFile;
    protected Vector _exclude = new Vector();

    public BotExclusion() {
    }

    public void load(HTTP http, String link) throws MalformedURLException, UnknownHostException, IOException {
        boolean isE = false;
        URL url = new URL(link);
        URL botUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), "/robots.txt");
        this._robotFile = botUrl.toString();
        http.send(this._robotFile, (String)null);
        StringReader stringReader = new StringReader(http.getBody());
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        String s;
        while((s = bufferedReader.readLine()) != null) {
            s = s.trim();
            if (s.length() >= 1 && s.charAt(0) != '#') {
                int index = s.indexOf(58);
                if (index != -1) {
                    String s1 = s.substring(0, index);
                    String s2 = s.substring(index + 1).trim();
                    if (s1.equalsIgnoreCase("User-agent")) {
                        isE = false;
                        if (s2.equals("*")) {
                            isE = true;
                        } else if (s2.equalsIgnoreCase(http.getAgent())) {
                            isE = true;
                        }
                    }

                    if (isE && s1.equalsIgnoreCase("disallow")) {
                        URL url1 = new URL(new URL(this._robotFile), s2);
                        if (!this.isExcluded(url1.toString())) {
                            this._exclude.addElement(url1.toString());
                        }
                    }
                }
            }
        }

    }

    public boolean isExcluded(String str) {
        Enumeration enumeration = this._exclude.elements();

        while(enumeration.hasMoreElements()) {
            String element = (String)enumeration.nextElement();
            if (element.startsWith(str)) {
                return true;
            }
        }

        return false;
    }

    public Vector getExclude() {
        return this._exclude;
    }

    public String getRobotFile() {
        return this._robotFile;
    }
}
