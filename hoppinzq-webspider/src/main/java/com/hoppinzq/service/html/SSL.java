package com.hoppinzq.service.html;

import com.sun.net.ssl.internal.ssl.Provider;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;

/**
 * @author: zq
 */
public class SSL {
    protected static SSLSocketFactory _factory = null;

    private SSL() {
    }

    public static Socket getSSLSocket(String host, int port) throws UnknownHostException, IOException {
        if (_factory == null) {
            Security.addProvider(new Provider());
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            _factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        }

        SSLSocket sslSocket = (SSLSocket)_factory.createSocket(host, port);
        return sslSocket;
    }
}
