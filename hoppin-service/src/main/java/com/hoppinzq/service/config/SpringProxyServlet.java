package com.hoppinzq.service.config;


import com.hoppinzq.service.SpringUtils;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.brap.auth.AuthenticationNotRequiredAuthenticator;
import com.hoppinzq.service.brap.auth.AuthenticationRequiredAuthorizer;
import com.hoppinzq.service.brap.auth.SingleUsernamePasswordAuthenticator;
import com.hoppinzq.service.brap.enums.ServerEnum;
import com.hoppinzq.service.brap.modification.ChangesIgnoredModificationManager;
import com.hoppinzq.service.brap.service.ServiceMessage;
import com.hoppinzq.service.brap.service.ServiceRegisterBean;
import com.hoppinzq.service.brap.service.ServiceWrapper;
import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.servlet.ProxyServlet;
import com.hoppinzq.service.util.AopTargetUtil;
import com.hoppinzq.service.util.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;

/**
 * 注册内部服务
 * 内部服务的标志为服务类被@serviceRegister注解所环绕
 */
public class SpringProxyServlet extends ProxyServlet {
    protected ApplicationContext applicationContext;

//    @Value("${zqServer.name:zhangqi}")
//    private String name;
//
//    @Value("${zqServer.password:123456}")
//    private String password;

    private static Logger logger = LoggerFactory.getLogger(SpringProxyServlet.class);

    public void createServiceWrapper() {
        logger.debug("开始注册内部服务");
        SingleUsernamePasswordAuthenticator singleUsernamePasswordAuthenticator=new SingleUsernamePasswordAuthenticator();
        singleUsernamePasswordAuthenticator.setUsername("zhangqi");
        singleUsernamePasswordAuthenticator.setPassword("123456");
        applicationContext= SpringUtils.getApplicationContext();
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
                    System.err.println("服务注册失败!失败类："+className);
                    System.exit(-1);
                }
                type=bean.getClass();
            }else{
                type = applicationContext.getType(className);
            }
            ServiceRegister serviceRegister = type.getAnnotation(ServiceRegister.class);
            if(serviceRegister!=null){
                ServiceWrapper serviceWrapper=new ServiceWrapper();
                ServiceMessage serviceMessage=new ServiceMessage();
                serviceMessage.setServiceIP(IPUtils.getIpAddress());
                serviceMessage.setServiceTitle(serviceRegister.title());
                serviceMessage.setServiceDescription(serviceRegister.description());
                serviceMessage.setTimeout(serviceRegister.timeout());
                serviceMessage.setServiceType(ServerEnum.INNER);//内部服务
                serviceWrapper.setServiceMessage(serviceMessage);
                if(proxyBean!=null){
                    serviceWrapper.setService(proxyBean);
                }else{
                    serviceWrapper.setService(bean);
                }
                //用户名密码校验
                serviceWrapper.setAuthenticationProvider(singleUsernamePasswordAuthenticator);
                if (serviceWrapper.getServiceMessage() == null)
                    serviceWrapper.setServiceMessage(new ServiceMessage());
                if (serviceWrapper.getAuthenticationProvider() == null)
                    serviceWrapper.setAuthenticationProvider(new AuthenticationNotRequiredAuthenticator());
                if (serviceWrapper.getAuthorizationProvider() == null)
                    //校验调用者凭证
                    serviceWrapper.setAuthorizationProvider(new AuthenticationRequiredAuthorizer());

                    //不交易
                    //serviceWrapper.setAuthorizationProvider(new AuthenticationNotRequiredAuthorizer());
                if (serviceWrapper.getModificationManager() == null)
                    serviceWrapper.setModificationManager( new ChangesIgnoredModificationManager());
                if (serviceWrapper.getServiceRegisterBean() == null)
                    serviceWrapper.setServiceRegisterBean( new ServiceRegisterBean());
                ServiceStore.serviceWrapperList.add(serviceWrapper);
            }
        }
    }
}
