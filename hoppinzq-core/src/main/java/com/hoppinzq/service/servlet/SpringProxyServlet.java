package com.hoppinzq.service.servlet;


import com.hoppinzq.service.auth.*;
import com.hoppinzq.service.bean.ServiceMessage;
import com.hoppinzq.service.modification.ModificationManager;
import com.hoppinzq.service.modification.SetterModificationManager;
import com.hoppinzq.service.bean.ServiceMethodBean;
import com.hoppinzq.service.util.EncryptUtil;
import com.hoppinzq.service.util.SpringUtils;
import com.hoppinzq.service.modification.NotModificationManager;
import com.hoppinzq.service.bean.ServiceRegisterBean;
import com.hoppinzq.service.bean.ServiceWrapper;
import com.hoppinzq.service.cache.ServiceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 注册内部服务
 * 只要服务类被@ServiceRegister注解所环绕，就会被作为内部服务注册
 */
public class SpringProxyServlet extends ProxyServlet {


    private static Logger logger = LoggerFactory.getLogger(SpringProxyServlet.class);

    public void createServiceWrapper() {
        List<ServiceWrapper> serviceWrappers=ServiceStore.serviceWrapperList;
        super.setApplicationContext(SpringUtils.getApplicationContext());
        super.setServiceWrappers(serviceWrappers);
        super.createServiceWrapper();
    }



//    public void checkService(){
//        List<ServiceWrapper> list= ServiceStore.serviceWrapperList;
//        for(ServiceWrapper serviceWrapper:list){
//            if(serviceWrapper.getService()==null){
//                ServiceRegisterBean serviceRegisterBean=serviceWrapper.getServiceRegisterBean();
//                try{
//                    AuthenticationProvider authenticationProvider = serviceWrapper.getAuthenticationProvider();
//                    if (authenticationProvider instanceof SimpleUserCheckAuthenticator) {
//                        UserPrincipal upp= new UserPrincipal(((SimpleUserCheckAuthenticator) authenticationProvider).getUsername(),
//                                ((SimpleUserCheckAuthenticator) authenticationProvider).getPassword());
//                        //Class cls=Class.forName(serviceRegisterBean.getServiceFullName());
//
//                        Object o=ServiceProxyFactory.createProxy(serviceRegisterBean.getService(), "http://localhost:8802/service", upp);
//                        System.err.println(((Test)o).test(1));
//                        //Test test=ServiceProxyFactory.createProxy(Test.class, "http://localhost:8802/service", upp);
//                        //System.err.println(test.test(1));
//
//                        //Method method=.getMethod("test", String.class);
//                        //Object o=method.invoke(serviceWrapper.getService(), 1);
//                        int i=123;
//                    }else if(authenticationProvider instanceof DbUserCheckAuthenticator){
//                    }else if(authenticationProvider instanceof AuthenticationNotCheckAuthenticator){
//                    }else{
//                        Anonymous anonymous=new Anonymous();
//                    }
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                    //serviceRegisterBean.setAvailable(Boolean.FALSE);
//                }
//                finally {
//
//                }
//            }
//        }
//    }

    public String respondServiceHtml(){
        StringBuilder s = new StringBuilder();
        s.append("<style type=\"text/css\">");
        s.append("table { width: 100%; border-collapse: collapse; border: 1px solid #ccc; }");
        s.append("td, th { padding: 5px; } ");
        s.append("td { border: 1px solid #ccc; margin: 0; }");
        s.append("th { text-align: left; background-color: #5FCB71; }");
        s.append(".useless {background-color: #f44336}");
        s.append("td.returnType { text-align: right;width: 20%; }");
        s.append("</style>");
        s.append("<h1 style='\"text-align\",\"center\"'>注册中心</h1>");
        for(ServiceWrapper serviceWrapper:serviceWrappers){
            if(serviceWrapper.isVisible()){
                Object bean=getWrapperServicePreBean(serviceWrapper);
                if(bean==null){
                    ServiceRegisterBean registerBean=serviceWrapper.getServiceRegisterBean();
                    s.append("<h1>服务ID:"+serviceWrapper.getId()+",服务名：" + registerBean.getServiceName() + "</h1>");
                    s.append("<h3>外部服务</h3>");
                    ServiceMessage serviceMessage=serviceWrapper.getServiceMessage();
                    s.append("<h3>服务所在IP:"+serviceMessage.getServiceIP()+",服务暴露端口号:"+serviceMessage.getServicePort()+",服务调用前缀:"+serviceMessage.getServicePrefix()+",服务调用超时时间:"+serviceMessage.getTimeout()+"</h3>");
                    s.append("<h3>服务标题:"+serviceMessage.getServiceTitle()+",服务描述:"+serviceMessage.getServiceDescription()+",服务配置文件:<a onclick='alert(\"禁止查看！\")' href='javaScript:void(0)'>配置文件</a></h3>");

                    AuthenticationProvider authenticationProvider = serviceWrapper.getAuthenticationProvider();
                    if (authenticationProvider instanceof SimpleUserCheckAuthenticator) {
                        s.append("<h3>验证用户方式：用户名密码组合校验</h3>");
                        s.append("<h3>服务提供者："+((SimpleUserCheckAuthenticator) authenticationProvider).getUsername()+",密码;"+ EncryptUtil.MD5(((SimpleUserCheckAuthenticator) authenticationProvider).getPassword()) +"</h3>");
                    }else if(authenticationProvider instanceof DbUserCheckAuthenticator){
                        s.append("<h3>验证用户方式：数据库表数据校验</h3>");
                    }else if(authenticationProvider instanceof AuthenticationNotCheckAuthenticator){
                        s.append("<h3>验证用户方式：不校验用户身份</h3>");
                    }else{
                        s.append("<h3>验证用户方式：其他方式校验</h3>");
                    }
                    AuthorizationProvider authorizationProvider=serviceWrapper.getAuthorizationProvider();
                    if (authorizationProvider instanceof AuthenticationNotCheckAuthorizer) {
                        s.append("<h3>服务调用权限验证方式：授权所有调用</h3>");
                    }else if(authorizationProvider instanceof AuthenticationCheckAuthorizer){
                        s.append("<h3>服务调用权限验证方式：授权通过身份校验的调用</h3>");
                    }else{
                        s.append("<h3>服务调用权限验证方式：其他方式鉴权</h3>");
                    }

                    ModificationManager modificationManager=serviceWrapper.getModificationManager();
                    if (modificationManager instanceof NotModificationManager) {
                        s.append("<h3>服务跟踪方式：不跟踪服务对参数的修改</h3>");
                    }else if(modificationManager instanceof SetterModificationManager){
                        s.append("<h3>服务跟踪方式：跟踪服务对参数的修改</h3>");
                    }else{
                        s.append("<h3>服务跟踪方式：其他方式跟踪</h3>");
                    }
                    s.append("<table><tr>");
                    if(serviceWrapper.isAvailable()){
                        s.append("<th colspan=\"2\">服务外方法</th></tr>");
                    }else{
                        s.append("<th class=\"useless\" colspan=\"2\">服务已不可用</th></tr>");
                    }
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
                    s.append("<h2>服务ID:"+serviceWrapper.getId()+",服务名：" + bean.getClass().getSimpleName() + "</h2>");
                    s.append("<h3>内部服务</h3>");
                    AuthenticationProvider authenticationProvider = serviceWrapper.getAuthenticationProvider();
                    if (authenticationProvider instanceof SimpleUserCheckAuthenticator) {
                        s.append("<h3>验证用户方式：用户名密码组合校验</h3>");
                    }else if(authenticationProvider instanceof DbUserCheckAuthenticator){
                        s.append("<h3>验证用户方式：数据库表数据校验</h3>");
                    }else if(authenticationProvider instanceof AuthenticationNotCheckAuthenticator){
                        s.append("<h3>验证用户方式：不校验用户身份</h3>");
                    }else{
                        s.append("<h3>验证用户方式：其他方式校验</h3>");
                    }

                    AuthorizationProvider authorizationProvider=serviceWrapper.getAuthorizationProvider();
                    if (authorizationProvider instanceof AuthenticationNotCheckAuthorizer) {
                        s.append("<h3>服务调用权限验证方式：授权所有调用</h3>");
                    }else if(authorizationProvider instanceof AuthenticationCheckAuthorizer){
                        s.append("<h3>服务调用权限验证方式：授权通过身份校验的调用</h3>");
                    }else{
                        s.append("<h3>服务调用权限验证方式：其他方式鉴权</h3>");
                    }

                    ModificationManager modificationManager=serviceWrapper.getModificationManager();
                    if (modificationManager instanceof NotModificationManager) {
                        s.append("<h3>服务跟踪方式：不跟踪服务对参数的修改</h3>");
                    }else if(modificationManager instanceof SetterModificationManager){
                        s.append("<h3>服务跟踪方式：跟踪服务对参数的修改</h3>");
                    }else{
                        s.append("<h3>服务跟踪方式：其他方式跟踪</h3>");
                    }
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
}
