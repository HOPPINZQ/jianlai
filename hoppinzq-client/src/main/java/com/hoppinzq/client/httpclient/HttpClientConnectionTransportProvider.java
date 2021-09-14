package com.hoppinzq.client.httpclient;

import com.hoppinzq.client.MethodInvocationHandler;
import com.hoppinzq.client.TransportProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.IOException;

/**
 * <p>Minimalistic provider for Apache HttpClient based transport. You will probably
 * want to either extend this class or implement TransportProvider directly
 * to provide a configured HttpClient. It is recommended to extend and override getHttpClient(),
 * and possibly include a pooling mechanism.</p>
 *
 * <p>To use this provider as default, you can:</p>
 * <pre>MethodInvocationFactory.setDefaultTransportProvider(new HttpClientConnectionTransportProvider()).</pre>
 *
 * <p>Alternatively you can supply this or another implementation to <code>ServiceProxyFactory.createProxy()</code>
 * by instantiating a <code>MethodInvocationHandler</code> with your <code>TransportProvider</code>.</p>
 *
 * *<p>针对基于Apache HttpClient的传输的最低限度的提供程序。你可能会
 * *要扩展此类或直接实现TransportProvider吗
 * *提供已配置的HttpClient。建议扩展并覆盖getHttpClient（），
 * *并可能包括一个池机制</p>
 * *<p>要使用此提供程序作为默认设置，您可以：</p>
 * *<pre>MethodInvocationFactory.setDefaultTransportProvider（新的HttpClientConnectionTransportProvider（））。</pre>
 * *<p>或者，您可以将此实现或其他实现提供给<code>ServiceProxyFactory.createProxy（）</code>
 * *通过使用<code>TransportProvider</code>实例化<code>MethodInvocationHandler</code></p>
 */
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
