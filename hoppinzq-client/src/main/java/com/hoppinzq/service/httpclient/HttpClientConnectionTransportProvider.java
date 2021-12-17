package com.hoppinzq.service.httpclient;

import com.hoppinzq.service.MethodInvocationHandler;
import com.hoppinzq.service.TransportProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.IOException;

public class HttpClientConnectionTransportProvider implements TransportProvider<HttpClientTransportSession> {
    public HttpClientTransportSession createSession(MethodInvocationHandler invocationHandler) {
        HttpClient httpClient = getHttpClient();
        return new HttpClientTransportSession(httpClient, invocationHandler);
    }

    protected HttpClient getHttpClient() {
        return new DefaultHttpClient();
    }

    public void endSession(HttpClientTransportSession session, MethodInvocationHandler invocationHandler) {
        try {
            session.getHttpResponse().getEntity().getContent().close();
        } catch (IOException ignored) {
        }
    }
}
