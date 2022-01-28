package com.hoppinzq.service.servlet;

import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.bean.PropertyBean;
import com.hoppinzq.service.bean.ServiceWrapper;
import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.config.RetryRegisterService;
import com.hoppinzq.service.exception.RemotingException;
import com.hoppinzq.service.interfaceService.RegisterServer;
import com.hoppinzq.service.task.TaskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 注册内部服务
 * 只要服务类被@ServiceRegister注解所环绕，就会被作为内部服务注册
 */
public class SpringProxyServlet extends ProxyServlet {

    private static Logger logger = LoggerFactory.getLogger(SpringProxyServlet.class);

    public void createServiceWrapper() {
        List<ServiceWrapper> serviceWrappers= ServiceStore.serviceWrapperList;
        super.setServiceWrappers(serviceWrappers);
        super.createServiceWrapper();
        try{
            registerServiceIntoCore();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void registerServiceIntoCore() throws Exception{
        PropertyBean propertyBean=this.getPropertyBean();
        //一直注册，每分钟尝试一次
        TaskStore.taskQueue.push(new RetryRegisterService(propertyBean.getRetryCount(),propertyBean.getRetryTime(),propertyBean.getAlwaysRetry()) {
            @Override
            protected Object toDo() throws RemotingException {
                UserPrincipal upp = new UserPrincipal(propertyBean.getUserName(), propertyBean.getPassword());
                RegisterServer service = ServiceProxyFactory.createProxy(RegisterServer.class, propertyBean.getServerCenter(), upp);
                List<ServiceWrapper> serviceWrappers=modWrapper();
                service.insertServices(serviceWrappers);
                logger.info("向注册中心注册服务成功！");
                return true;
            }
        });
    }
}
