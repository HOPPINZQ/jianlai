package com.hoppinzq.service.servlet;

import com.hoppinzq.service.brap.auth.*;
import com.hoppinzq.service.brap.common.InputStreamArgumentPlaceholder;
import com.hoppinzq.service.brap.common.InvocationRequest;
import com.hoppinzq.service.brap.common.InvocationResponse;
import com.hoppinzq.service.brap.exception.RemotingException;
import com.hoppinzq.service.brap.modification.ChangesIgnoredModificationManager;
import com.hoppinzq.service.brap.modification.ModificationManager;
import com.hoppinzq.service.brap.service.ServiceWrapper;
import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.util.AopTargetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 公开处理远程服务
 */
public class ProxyServlet implements Servlet {

    @Autowired
    private ApplicationContext applicationContext;
    private static Logger logger = LoggerFactory.getLogger(ProxyServlet.class);

    private static final Integer DEFAULT_STREAM_BUFFER_SIZE = 16384;

    public final String INIT_PARAM_AUTHENTICATION_PROVIDER = "authenticationProvider";
    public final String INIT_PARAM_AUTHORIZATION_PROVIDER = "authorizationProvider";
    public final String INIT_PARAM_MODIFICATION_MANAGER = "modificationManager";
    public final String INIT_PARAM_SERVICE = "service";

    protected List<ServiceWrapper> serviceWrappers= ServiceStore.serviceWrapperList;

    protected ServletConfig servletConfig;

    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig = servletConfig;
        try {
            createServiceWrapper();
        } catch (Exception e) {
            throw new ServletException("未能实例化serviceWrapper", e);
        }
    }

    /**
     * 重写此方法以控制创建服务包装
     */
    public void createServiceWrapper() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //注册服务
    }

    /**
     * 重写此方法以配置不同的授权提供程序
     * 授权每个经过身份验证的调用
     */
    protected AuthorizationProvider getAuthorizationProvider() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (servletConfig.getInitParameter(INIT_PARAM_AUTHORIZATION_PROVIDER) != null)
            return (AuthorizationProvider) Class.forName(servletConfig.getInitParameter(INIT_PARAM_AUTHORIZATION_PROVIDER)).newInstance();

        return new AuthenticationRequiredAuthorizer();
    }

    /**
     * 重写此方法以配置不同的身份验证提供程序
     */
    protected AuthenticationProvider getAuthenticationProvider() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (servletConfig.getInitParameter(INIT_PARAM_AUTHENTICATION_PROVIDER) != null)
            return (AuthenticationProvider) Class.forName(servletConfig.getInitParameter(INIT_PARAM_AUTHENTICATION_PROVIDER)).newInstance();

        return new AuthenticationNotRequiredAuthenticator();
    }

    /**
     * 提供要公开的服务
     */
    protected Object getService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return Class.forName(servletConfig.getInitParameter(INIT_PARAM_SERVICE)).newInstance();
    }


    /**
     * 服务方法执行调用请求的实际反序列化并返回
     * *ServletResponse主体中的调用响应。
     * *<p/>
     * *标准Java对象序列化/反序列化用于检索和设置调用
     * *请求/答复。
     * *已配置的<code>AuthenticationProvider</code>和<code>AuthenticationProvider</code>
     * *征求意见。
     * *<p/>
     * *<code>AuthenticationContext</code>中的ThreadLocal保留在
     * *身份验证，以便AuthorizationProvider和任何服务都可以使用它
     * *它通过<code>AuthenticationContext#getPrincipal（）</code>获得主体。
     * *建议您使用现有域对象AllowAllAuthorizer进行身份验证
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        logger.debug("开始调用服务");
        //request.getRequestDispatcher("http://localhost:8081/cache/apiCache").forward(request,response);
        AuthenticationContext.enter();
        InvocationResponse invocationResponse = null;
        InvocationRequest invocationRequest = null;
        Method method = null;
        Object result = null;
        try {
            invocationResponse = new InvocationResponse();
            ServletInputStream servletInputStream=request.getInputStream();
            invocationRequest=(InvocationRequest)new ObjectInputStream(servletInputStream).readObject();

            ServiceWrapper serviceWrapper=this.checkWrapper(serviceWrappers,invocationRequest.getServiceName());
            if(serviceWrapper==null){
                throw new RuntimeException("该服务不存在，或者是已停用！");
            }
            serviceWrapper.getAuthenticationProvider().authenticate(invocationRequest);
            serviceWrapper.getAuthorizationProvider().authorize(invocationRequest);
            Object[] proxiedParameters = serviceWrapper.getModificationManager().applyModificationScheme(invocationRequest.getParameters());
            method = getMethod(serviceWrapper,invocationRequest.getMethodName(), invocationRequest.getParameterTypes());

            if (invocationRequest.getParameters() != null) {
                for (int i = 0; i < invocationRequest.getParameters().length; i ++) {
                    if (invocationRequest.getParameters()[i] != null && InputStreamArgumentPlaceholder.class.equals(invocationRequest.getParameters()[i].getClass())) {
                        proxiedParameters[i] = request.getInputStream();
                        break;
                    }
                }
            }

            preMethodInvocation();
            result = method.invoke(serviceWrapper.getService(), proxiedParameters);
            invocationResponse.setResult((Serializable) result);
            invocationResponse.setModifications(serviceWrapper.getModificationManager().getModifications());

        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) e;
                invocationResponse.setException(ite.getTargetException());
            } else {
                if (method != null && method.getExceptionTypes() != null) {
                    for (Class exType : method.getExceptionTypes()) {
                        if (exType.isAssignableFrom(e.getClass()))
                            invocationResponse.setException(e);
                    }
                }
                invocationResponse.setException(new RemotingException(e));
            }
        } finally {
            AuthenticationContext.exit();
            postMethodInvocation();
            if (result != null && result instanceof InputStream) {
                streamResultToResponse(result, response);
            } else {
                try {
                    if (invocationRequest != null) {
                        ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
                        out.writeObject(invocationResponse);
                        out.close();
                    } else {
                        respondWithInterfaceDeclaration(response);
                    }
                } catch (Exception e) {
                    InvocationResponse reporter = new InvocationResponse();
                    reporter.setException(new RuntimeException(e.getClass() + " error while writing result: " + e.getMessage()));

                    ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
                    out.writeObject(reporter);
                    out.close();
                }
            }
        }
    }

    /**
     * 根据服务名找到服务
     * @param serviceWrappers
     * @param serviceName
     * @return
     */
    private ServiceWrapper checkWrapper(List<ServiceWrapper> serviceWrappers, String serviceName){

        for(ServiceWrapper serviceWrapper:serviceWrappers){
            Object bean=getWrapperServicePreBean(serviceWrapper);
            Class<?>[] cs=bean.getClass().getInterfaces();
            for(Class c:cs){
                int index=c.getName().lastIndexOf(".")+1;
                String className=c.getName().substring(index);
                if(className.equals(serviceName)){
                    return serviceWrapper;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param serviceWrapper
     * @return
     */
    private Object getWrapperServicePreBean(ServiceWrapper serviceWrapper){
        Object bean=serviceWrapper.getService();
        if (bean instanceof Advised) {
            try{
                return AopTargetUtil.getTarget(bean);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return bean;
    }

    /**
     * 没有可用的调用请求，假设请求不是由客户端发出的。
     * 返回描述服务接口的HTML
     * @param response
     */
    private void respondWithInterfaceDeclaration(ServletResponse response) throws IOException {
        StringBuilder s = new StringBuilder();
        s.append("<style type=\"text/css\">");
        s.append("table { width: 100%; border-collapse: collapse; border: 1px solid #ccc; }");
        s.append("td, th { padding: 5px; } ");
        s.append("td { border: 1px solid #ccc; margin: 0; }");
        s.append("th { text-align: left; background-color: #5FCB71; }");
        s.append("td.returnType { text-align: right;width: 20%; }");
        s.append("</style>");
        for(ServiceWrapper serviceWrapper:serviceWrappers){
            Object bean=getWrapperServicePreBean(serviceWrapper);
            s.append("<h1>服务名：" + bean.getClass().getSimpleName() + "</h1>");
            s.append("<table><tr><th colspan=\"2\">服务内方法</th></tr>");
            for (Method method : bean.getClass().getDeclaredMethods()) {
                s.append("<tr><td class=\"returnType\">" + method.getReturnType().getSimpleName() + "</td><td class=\"method\">");
                s.append("<strong>" + method.getName() + "</strong>(");
                Class[] params = method.getParameterTypes();
                if (params != null && params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        if (i > 0)
                            s.append(", ");
                        s.append(params[i].getSimpleName() + " arg" + i);
                    }
                }
                s.append(")</td></tr>");
            }
            s.append("</table>");
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        OutputStream out = response.getOutputStream();
        out.write(s.toString().getBytes());
        out.close();
    }

    /**
     * 重写该方法以在对服务的方法进行开票后执行自定义工作
     */
    protected void postMethodInvocation() {

    }


    /**
     * 重写该方法以在服务上调用方法之前执行一些其他事情
     */
    protected void preMethodInvocation() {

    }

    private void streamResultToResponse(Object result, ServletResponse response) throws IOException {
        InputStream in = (InputStream) result;
        OutputStream out = response.getOutputStream();
        byte[] buf = new byte[getStreamBufferSize()];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
    }

    public ModificationManager getModificationManager() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (servletConfig.getInitParameter(INIT_PARAM_MODIFICATION_MANAGER) != null)
            return (ModificationManager) Class.forName(servletConfig.getInitParameter(INIT_PARAM_MODIFICATION_MANAGER)).newInstance();

        return new ChangesIgnoredModificationManager();
    }

    /**
     * 在包装好的服务类上调用注册服务的内部方法
     * @param methodName
     * @param parameterTypes
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(ServiceWrapper serviceWrapper, String methodName, Class[] parameterTypes) throws NoSuchMethodException {
        Class serviceClass = serviceWrapper.getService().getClass();

        while (serviceClass != null) {
            try {
                return serviceClass.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                serviceClass = serviceClass.getSuperclass();
            }
        }

        throw new NoSuchMethodException(methodName);
    }

    public String getServletInfo() {
        return getClass().getCanonicalName();
    }

    public void destroy() {

    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public Integer getStreamBufferSize() {
        return DEFAULT_STREAM_BUFFER_SIZE;
    }
}
