package com.hoppinzq.service.html;

import com.hoppinzq.service.util.Base64OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: zq
 */
public class URLUtility {
    private URLUtility() {
    }

    public static URL stripQuery(URL url) throws MalformedURLException {
        String file = url.getFile();
        int index = file.indexOf("?");
        if (index == -1) {
            return url;
        } else {
            file = file.substring(0, index);
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
        }
    }

    public static URL stripAnhcor(URL url) throws MalformedURLException {
        String file = url.getFile();
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
    }

    public static String base64Encode(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream);

        try {
            base64OutputStream.write(str.getBytes());
            base64OutputStream.flush();
        } catch (IOException var4) {
        }

        return byteArrayOutputStream.toString();
    }

    public static String resolveBase(String str1, String str2) {
        int index = str1.indexOf(58);
        String temp;
        if (index != -1) {
            temp = str1.substring(0, index + 1);
            str1 = "http:" + str1.substring(index + 1);
        } else {
            temp = null;
        }

        URL url;
        try {
            url = new URL(new URL(str1), str2);
        } catch (MalformedURLException exception) {
            return "";
        }

        if (temp != null) {
            str1 = url.toString();
            index = str1.indexOf(58);
            if (index != -1) {
                str1 = str1.substring(index + 1);
            }

            str1 = temp + str1;
            return str1;
        } else {
            return url.toString();
        }
    }
}
