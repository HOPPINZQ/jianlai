package com.hoppinzq.service.servlet;

import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.auth.*;
import com.hoppinzq.service.bean.*;
import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.common.InputStreamArgument;
import com.hoppinzq.service.common.InvocationRequest;
import com.hoppinzq.service.common.InvocationResponse;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.enums.ServiceTypeEnum;
import com.hoppinzq.service.exception.RemotingException;
import com.hoppinzq.service.modification.NotModificationManager;
import com.hoppinzq.service.modification.ModificationManager;
import com.hoppinzq.service.modification.SetterModificationManager;
import com.hoppinzq.service.service.HeartbeatService;
import com.hoppinzq.service.service.HeartbeatServiceImpl;
import com.hoppinzq.service.util.AopTargetUtil;
import com.hoppinzq.service.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;

import javax.servlet.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 公开处理远程服务
 */
public class ProxyServlet implements Servlet {

    private ApplicationContext applicationContext;
    private PropertyBean propertyBean;

    private static Logger logger = LoggerFactory.getLogger(ProxyServlet.class);

    private static final Integer DEFAULT_STREAM_BUFFER_SIZE = 16384;

    public final String INIT_PARAM_AUTHENTICATION_PROVIDER = "authenticationProvider";
    public final String INIT_PARAM_AUTHORIZATION_PROVIDER = "authorizationProvider";
    public final String INIT_PARAM_MODIFICATION_MANAGER = "modificationManager";
    public final String INIT_PARAM_SERVICE = "service";

    public List<ServiceWrapper> serviceWrappers;

    public List<ServiceWrapper> getServiceWrappers() {
        return serviceWrappers;
    }

    public void setServiceWrappers(List<ServiceWrapper> serviceWrappers) {
        this.serviceWrappers = serviceWrappers;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public PropertyBean getPropertyBean() {
        return propertyBean;
    }

    public void setPropertyBean(PropertyBean propertyBean) {
        this.propertyBean = propertyBean;
    }

    public ServletConfig servletConfig;

    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig = servletConfig;
        propertyBean.setId(UUIDUtil.getUUID());
        try {
            createServiceWrapper();
            registerHeartbeatServiceNotCheck(new HeartbeatServiceImpl());
        } catch (Exception e) {
            throw new ServletException("未能实例化serviceWrapper", e);
        }
    }

    /**
     * 重写此方法以控制创建服务包装
     */
    public void createServiceWrapper() {
        SimpleUserCheckAuthenticator singleUsernamePasswordAuthenticator=new SimpleUserCheckAuthenticator();
        singleUsernamePasswordAuthenticator.setUsername(propertyBean.getUserName());
        singleUsernamePasswordAuthenticator.setPassword(propertyBean.getPassword());
        String[] classNames=applicationContext.getBeanDefinitionNames();
        Class<?> type;
        for(String className:classNames){
            Object bean = applicationContext.getBean(className);
            Object proxyBean=null;
            if (bean == this) {
                continue;
            }
            if (bean instanceof Advised) {
                try{
                    proxyBean=bean;
                    bean= AopTargetUtil.getTarget(bean);
                }catch (Exception ex){
                    logger.error("服务注册失败!启动失败！失败类："+className);
                    System.exit(-1);
                }
                type=bean.getClass();
            }else{
                type = applicationContext.getType(className);
            }
            ServiceRegister serviceRegister = type.getAnnotation(ServiceRegister.class);
            if(serviceRegister!=null&&serviceRegister.registerType()== ServiceRegister.RegisterType.AUTO){
                ServiceWrapper serviceWrapper=new ServiceWrapper();
                //注册内部服务
                serviceWrapper.setId(propertyBean.getId());
                serviceWrapper.setServiceMessage(serviceWrapper.createInnerServiceMessage(propertyBean,serviceRegister.title(),serviceRegister.description(),serviceRegister.timeout()));
                if(proxyBean!=null){
                    serviceWrapper.setService(proxyBean);
                }else{
                    serviceWrapper.setService(bean);
                }
                //为内服服务注册用户校验规则，鉴权规则，监听规则及包装细节类（外部服务独有，因为内服服务可以通过反射获取细节）
                serviceWrapper.setAuthenticationProvider(singleUsernamePasswordAuthenticator);
                if (serviceWrapper.getServiceMessage() == null)
                    serviceWrapper.setServiceMessage(new ServiceMessage());
                if (serviceWrapper.getAuthenticationProvider() == null)
                    serviceWrapper.setAuthenticationProvider(new AuthenticationNotCheckAuthenticator());
                if (serviceWrapper.getAuthorizationProvider() == null)
                    //校验调用者凭证
                    serviceWrapper.setAuthorizationProvider(new AuthenticationCheckAuthorizer());
                //不校验
                //serviceWrapper.setAuthorizationProvider(new AuthenticationNotRequiredAuthorizer());
                if (serviceWrapper.getModificationManager() == null)
                    serviceWrapper.setModificationManager(new NotModificationManager() );
                if (serviceWrapper.getServiceRegisterBean() == null)
                    serviceWrapper.setServiceRegisterBean( new ServiceRegisterBean());
                serviceWrappers.add(serviceWrapper);
            }
        }
    }

    /**
     * 注册心跳服务。该服务只是为了让注册中心调用该服务以判断客户端是否还可以正常通讯
     * @param obj
     */
    public final void registerHeartbeatServiceNotCheck(Object obj){
        if(obj instanceof HeartbeatService){
            if (obj instanceof Advised) {
                try{
                    obj= AopTargetUtil.getTarget(obj);
                }catch (Exception ex){
                    logger.error("注册心跳服务失败!启动失败");
                    System.exit(-1);
                }
            }
            ServiceWrapper serviceWrapper=new ServiceWrapper();
            serviceWrapper.setId(propertyBean.getId());
            serviceWrapper.setService(obj);
            serviceWrapper.setServiceTypeEnum(ServiceTypeEnum.HEARTBEAT);
            serviceWrapper.setAuthenticationProvider(new AuthenticationNotCheckAuthenticator());
            serviceWrapper.setAuthorizationProvider(new AuthenticationNotCheckAuthorizer());
            serviceWrapper.setModificationManager(new NotModificationManager());
            serviceWrapper.setServiceRegisterBean(new ServiceRegisterBean(Boolean.FALSE));
            serviceWrapper.setServiceMessage(new ServiceMessage(propertyBean.getIp(),propertyBean.getPort(),propertyBean.getPrefix(),ServerEnum.INNER));
            serviceWrappers.add(serviceWrapper);
        }else{
            logger.error("注册的不是心跳服务!");
        }
    }

    /**
     * 重写此方法以配置不同的授权提供程序
     * 授权每个经过身份验证的调用
     */
    public AuthorizationProvider getAuthorizationProvider() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (servletConfig.getInitParameter(INIT_PARAM_AUTHORIZATION_PROVIDER) != null)
            return (AuthorizationProvider) Class.forName(servletConfig.getInitParameter(INIT_PARAM_AUTHORIZATION_PROVIDER)).newInstance();

        return new AuthenticationCheckAuthorizer();
    }

    /**
     * 重写此方法以配置不同的身份验证提供程序
     */
    public AuthenticationProvider getAuthenticationProvider() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (servletConfig.getInitParameter(INIT_PARAM_AUTHENTICATION_PROVIDER) != null)
            return (AuthenticationProvider) Class.forName(servletConfig.getInitParameter(INIT_PARAM_AUTHENTICATION_PROVIDER)).newInstance();

        return new AuthenticationNotCheckAuthenticator();
    }

    /**
     * 提供要公开的服务
     */
    public Object getService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return Class.forName(servletConfig.getInitParameter(INIT_PARAM_SERVICE)).newInstance();
    }

    /**
     * 外部服务注册收集参数
     * @return
     */
    public List<ServiceWrapper> modWrapper() {
        List<ServiceWrapper> serviceWrappersCopyList = new ArrayList<>();
        for (ServiceWrapper serviceWrapper : serviceWrappers) {
            ServiceRegisterBean serviceRegisterBean = new ServiceRegisterBean();
            serviceRegisterBean.setVisible(serviceWrapper.isVisible());
            serviceRegisterBean.setServiceClass(serviceWrapper.getService().getClass().getInterfaces()[0]);
            PropertyBean propertyBean=this.propertyBean;
            ServiceMessage serviceMessage = new ServiceMessage(propertyBean.getIp(),propertyBean.getPort(),propertyBean.getPrefix(), ServerEnum.OUTER);
            ServiceWrapper serviceWrapperCopy=new ServiceWrapper(serviceWrapper.getId(),
                    null,serviceWrapper.getAuthenticationProvider(),serviceWrapper.getAuthorizationProvider(),
                    serviceWrapper.getModificationManager(),serviceMessage,serviceRegisterBean,
                    serviceWrapper.isVisible(),serviceWrapper.isAvailable(),serviceWrapper.getServiceTypeEnum());
            serviceWrappersCopyList.add(serviceWrapperCopy);
        }
        return serviceWrappersCopyList;
    }

    /**
     * 服务方法执行调用请求的实际反序列化并返回
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
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
                throw new RuntimeException("该服务不存在！");
            }
            if(serviceWrapper.getServiceRegisterBean()!=null&&!serviceWrapper.getServiceRegisterBean().isAvailable()){
                throw new RuntimeException("该服务已停用！");
            }
            serviceWrapper.getAuthenticationProvider().authenticate(invocationRequest);
            serviceWrapper.getAuthorizationProvider().authorize(invocationRequest);
            Object[] proxiedParameters = serviceWrapper.getModificationManager().applyModificationScheme(invocationRequest.getParameters());
            method = getMethod(serviceWrapper,invocationRequest.getMethodName(), invocationRequest.getParameterTypes());

            if (invocationRequest.getParameters() != null) {
                for (int i = 0; i < invocationRequest.getParameters().length; i ++) {
                    if (invocationRequest.getParameters()[i] != null && InputStreamArgument.class.equals(invocationRequest.getParameters()[i].getClass())) {
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
                        String resStr= respondServiceHtml();
                        respondWithInterfaceDeclaration(resStr,response);
                    }
                } catch (Exception e) {
                    InvocationResponse reporter = new InvocationResponse();
                    reporter.setException(new RuntimeException(e.getClass() + "写入结果时出错: " + e.getMessage()));

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
            if(serviceWrapper.isInnerService()){
                Object bean=getWrapperServicePreBean(serviceWrapper);
                Class<?>[] cs=bean.getClass().getInterfaces();
                for(Class c:cs){
                    String className=c.getSimpleName();
                    if(className.equals(serviceName)){
                        return serviceWrapper;
                    }
                }
            }else{
                ServiceRegisterBean serviceRegisterBean=serviceWrapper.getServiceRegisterBean();
                if(serviceName.equals(serviceRegisterBean.getServiceName())){
                    return serviceWrapper;
                }
            }
        }
        return null;
    }

    /**
     * 如果服务类被代理，获取代理前的服务类
     * @param serviceWrapper
     * @return
     */
    public Object getWrapperServicePreBean(ServiceWrapper serviceWrapper){
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
     * 暴露服务，没有可用的调用请求，假设请求不是由客户端发出的。
     * @return html
     */
    public String respondServiceHtml(){
        StringBuilder s = new StringBuilder();
        s.append("<style type=\"text/css\">");
        s.append("table { width: 100%; border-collapse: collapse; border: 1px solid #ccc; }");
        s.append("td, th { padding: 5px; } ");
        s.append("td { border: 1px solid #ccc; margin: 0; }");
        s.append("th { text-align: left; background-color: #5FCB71; }");
        s.append("td.returnType { text-align: right;width: 20%; }");
        s.append("</style>");
        for(ServiceWrapper serviceWrapper:serviceWrappers){
            if(serviceWrapper.isVisible()){
                Object bean=getWrapperServicePreBean(serviceWrapper);
                if(bean==null){
                    ServiceRegisterBean registerBean=serviceWrapper.getServiceRegisterBean();
                    s.append("<h1>服务名：" + registerBean.getServiceName() + "</h1>");
                    s.append("<h3>外部服务</h3>");
                    serviceWrapperExtra(s,serviceWrapper);
                    s.append("<table><tr><th colspan=\"2\">服务内方法</th></tr>");
                    for (ServiceMethodBean method : registerBean.getServiceMethodBeanList()) {
                        s.append("<tr><td class=\"returnType\">" + method.getMethodReturnType() + "</td><td class=\"method\">");
                        s.append("<strong>" + method.getMethodName() + "</strong>(");
                        String[] params = method.getMethodParamsType();
                        if (params != null && params.length > 0) {
                            for (int i = 0; i < params.length; i++) {
                                if (i > 0)
                                    s.append(", ");
                                s.append(params[i] + " arg" + i);
                            }
                        }
                        s.append(")</td></tr>");
                    }
                }else{
                    s.append("<h1>服务名：" + bean.getClass().getSimpleName() + "</h1>");
                    s.append("<h3>内部服务</h3>");
                    serviceWrapperExtra(s,serviceWrapper);
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
                }
                s.append("</table>");
            }
        }
        return s.toString();
    }

    private String serviceWrapperExtra(StringBuilder s,ServiceWrapper serviceWrapper){
        AuthenticationProvider authenticationProvider = serviceWrapper.getAuthenticationProvider();
        if (authenticationProvider instanceof SimpleUserCheckAuthenticator) {
            s.append("<h3>用户名密码组合校验</h3>");
        }else if(authenticationProvider instanceof DbUserCheckAuthenticator){
            s.append("<h3>数据库表数据校验</h3>");
        }else if(authenticationProvider instanceof AuthenticationNotCheckAuthenticator){
            s.append("<h3>不校验用户身份</h3>");
        }else{
            s.append("<h3>其他方式校验</h3>");
        }

        AuthorizationProvider authorizationProvider=serviceWrapper.getAuthorizationProvider();
        if (authorizationProvider instanceof AuthenticationNotCheckAuthorizer) {
            s.append("<h3>授权所有调用</h3>");
        }else if(authorizationProvider instanceof AuthenticationCheckAuthorizer){
            s.append("<h3>授权通过身份校验的调用</h3>");
        }else{
            s.append("<h3>其他方式鉴权</h3>");
        }

        ModificationManager modificationManager=serviceWrapper.getModificationManager();
        if (modificationManager instanceof NotModificationManager) {
            s.append("<h3>不跟踪服务对参数的修改</h3>");
        }else if(modificationManager instanceof SetterModificationManager){
            s.append("<h3>跟踪服务对参数的修改</h3>");
        }else{
            s.append("<h3>其他方式跟踪</h3>");
        }
        return s.toString();
    }

    /**
     * 返回描述服务接口的HTML
     * @param response
     */
    private void respondWithInterfaceDeclaration(String str,ServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        OutputStream out = response.getOutputStream();
        out.write(str.toString().getBytes());
        out.close();
    }

    /**
     * 重写该方法以在对服务的方法进行返回后执行自定义工作
     */
    public void postMethodInvocation() {

    }

    /**
     * 重写该方法以在服务上调用方法之前执行一些其他事情
     */
    public void preMethodInvocation() {

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

        return new NotModificationManager();
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
