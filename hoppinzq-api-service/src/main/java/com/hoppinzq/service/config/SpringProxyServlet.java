package com.hoppinzq.service.config;


import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.client.ServiceProxyFactory;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.exception.RemotingException;
import com.hoppinzq.service.service.ServiceMessage;
import com.hoppinzq.service.service.ServiceRegisterBean;
import com.hoppinzq.service.service.ServiceWrapper;
import com.hoppinzq.service.service.outService.RegisterServer;
import com.hoppinzq.service.servlet.ProxyServlet;
import com.hoppinzq.service.util.SpringUtils;
import com.hoppinzq.service.utils.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册内部服务
 * 只要服务类被@ServiceRegister注解所环绕，就会被作为内部服务注册
 * 重写ProxyServlet内的方法，以实现自定义注册方法
 *
 * 1、(可选)重写createServiceWrapper方法实现自定义包装服务和注册服务，注册服务的内部服务并向注册中心提供注册服务的副本
 * 2、(可选)重写getAuthorizationProvider方法以配置不同的授权提供程序，默认提供身份验证鉴权手段
 * 3、(可选)重写getAuthenticationProvider方法以配置不同的身份验证提供程序，默认提供不进行身份验证手段
 * 4、(可选)重写getService方法以提供服务想要公开的服务，有时候你可能想隐藏某些服务，return null即可，默认公开本服务所有注册的服务
 * 5、(可选)重写preMethodInvocation方法以在服务上调用方法之前执行一些其他操作，如定义调用开始时间等等
 * 6、(可选)重写postMethodInvocation方法以在对服务的方法返回后执行一些操作，如计算调用时长等等
 * 7、(可选)重写respondServiceHtml方法向调用者暴露想要公开的服务，即服务发现
 */
public class SpringProxyServlet extends ProxyServlet {
    protected ApplicationContext applicationContext;

//    @Value("${zqServer.name:zhangqi}")
//    private String name;
//
//    @Value("${zqServer.password:123456}")
//    private String password;

    @Value("${zqServerCenter.addr:http://localhost:8801/service}")
    private String serverCoreAddr;

    private static Logger logger = LoggerFactory.getLogger(SpringProxyServlet.class);

    public void createServiceWrapper(){
        List<ServiceWrapper> serviceWrappers = ServiceStore.serviceWrapperList;
        super.setApplicationContext(SpringUtils.getApplicationContext());
        super.setServiceWrappers(serviceWrappers);
        super.createServiceWrapper();
        try{
            UserPrincipal upp = new UserPrincipal("zhangqi", "123456");
            RegisterServer service = ServiceProxyFactory.createProxy(RegisterServer.class, "http://localhost:8801/service", upp);
            service.insertServices(modWrapper());
        }catch (RemotingException ex){
            ex.printStackTrace();
            if(ex.getMessage().indexOf("java.net.ConnectException")!=-1){
                logger.error("不能连接到注册中心，将会重新注册");
                //重试机制 Todo
            }else{
                logger.error(ex.getMessage());
            }
        }
    }

    private List<ServiceWrapper> modWrapper() {
        List<ServiceWrapper> serviceWrappers = ServiceStore.serviceWrapperList;
        List<ServiceWrapper> serviceWrappers1 = new ArrayList<>();
        for (ServiceWrapper serviceWrapper : serviceWrappers) {
            ServiceWrapper serviceWrapper1 = clone(serviceWrapper);
            serviceWrapper1.setService(null);
            ServiceRegisterBean serviceRegisterBean = new ServiceRegisterBean();
            serviceRegisterBean.setServiceClass(serviceWrapper.getService().getClass().getInterfaces()[0]);
            serviceWrapper1.setServiceRegisterBean(serviceRegisterBean);
            ServiceMessage serviceMessage = new ServiceMessage();
            serviceMessage.setServiceIP(IPUtils.getIpAddress());
            serviceMessage.setServiceType(ServerEnum.OUTER);
            serviceWrapper1.setServiceMessage(serviceMessage);
            serviceWrappers1.add(serviceWrapper1);
        }
        return serviceWrappers1;
    }

    /**
     * 序列化对象克隆，要克隆的对象必须实现序列化接口
     * @param obj
     * @param <T>
     * @return
     */
    private static <T> T clone(T obj) {
        T clone = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            ObjectInputStream ios = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            clone = (T) ios.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clone;
    }
}
