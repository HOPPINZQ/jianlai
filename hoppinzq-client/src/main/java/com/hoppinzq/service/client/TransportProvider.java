package com.hoppinzq.service.client;

/**
 * A TransportProvider is used to transport the method invocation call to the remote server. The default implementation
 * uses HttpURLConnection for the Transport, and there is an alternative for Apache HttpClient as well.
 *
 * You can change the default TransportProvider by calling the static method:
 * <pre>MethodInvocationFactory.setDefaultTransportProvider(yourImplementation).</pre>
 *
 * <p>Alternatively you can supply the TransportProvider to ServiceProxyFactory.createProxy()
 * by instantiating a <code>MethodInvocationHandler</code> with the <code>TransportProvider</code> of your choice.</p>
 *
 * @param <T>
 */

/**
 * TransportProvider用于将方法调用传输到远程服务器。默认实现
 * *使用HttpURLConnection进行传输，Apache HttpClient也有一个替代方案。
 * *您可以通过调用静态方法更改默认TransportProvider：
 * *<pre>MethodInvocationFactory.setDefaultTransportProvider（您的实现）。</pre>
 * *<p>或者，您可以向ServiceProxyFactory.createProxy（）提供TransportProvider
 * *通过使用您选择的<code>TransportProvider</code>实例化<code>MethodInvocationHandler</code></p>
 * @param <T>
 */
public interface TransportProvider<T extends TransportSession> {
    /**
     * Create a new session that can be used to transport the method invocation call to the remote server.
     * 创建可用于将方法调用传输到远程服务器的新会话。
     * @param invocationHandler 负责方法调用的调用处理程序。
     * @return TransportSession已启动并准备好处理一个调用请求。
     */
    T createSession(MethodInvocationHandler invocationHandler);

    /**
     * Called by the InvocationHandler after the method invocation has been performed. If you
     * implement some kind of pooling and need to deliver and underlying object back to a pool,
     * or need to perform other kinds of cleanup after the method invocation, this is the place to do it.
     * 在执行方法调用后由InvocationHandler调用。如果你
     * *实现某种类型的池，需要将对象和底层对象传递回池，
     * *或者需要在方法调用后执行其他类型的清理，这是执行清理的地方。
     * @param session The TransportSession that is now redundant
     * @param invocationHandler The invocationHandler that handled the method invocation.
     */
    void endSession(T session, MethodInvocationHandler invocationHandler);
}
