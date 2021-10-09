package com.hoppinzq.service.servlet;




import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.client.ServiceProxyFactory;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.config.RetryRegisterService;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.exception.RemotingException;
import com.hoppinzq.service.interfaceService.RegisterServer;
import com.hoppinzq.service.serviceBean.PropertyBean;
import com.hoppinzq.service.serviceBean.ServiceMessage;
import com.hoppinzq.service.serviceBean.ServiceRegisterBean;
import com.hoppinzq.service.serviceBean.ServiceWrapper;
import com.hoppinzq.service.task.TaskStore;
import com.hoppinzq.service.util.CloneUtil;
import com.hoppinzq.service.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册内部服务
 * 只要服务类被@ServiceRegister注解所环绕，就会被作为内部服务注册
 */
public class SpringProxyServlet extends ProxyServlet {

    private static Logger logger = LoggerFactory.getLogger(SpringProxyServlet.class);

    public void createServiceWrapper() {
        List<ServiceWrapper> serviceWrappers= ServiceStore.serviceWrapperList;
        super.setApplicationContext(SpringUtils.getApplicationContext());
        super.setServiceWrappers(serviceWrappers);
        super.createServiceWrapper();
        try{
            registerServiceIntoCore();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void registerServiceIntoCore() throws Exception{

        TaskStore.taskQueue.push(new RetryRegisterService(10,60000) {
            @Override
            protected Object toDo() throws RemotingException {
                UserPrincipal upp = new UserPrincipal("zhangqi", "123456");
                RegisterServer service = ServiceProxyFactory.createProxy(RegisterServer.class, "http://localhost:8801/service", upp);
                List<ServiceWrapper> serviceWrappers=modWrapper();
                service.insertServices(serviceWrappers);
                logger.info("向注册中心注册服务成功！");
                return true;
            }
        });
    }


    public List<ServiceWrapper> modWrapper() {
        List<ServiceWrapper> serviceWrappers = ServiceStore.serviceWrapperList;
        List<ServiceWrapper> serviceWrappers1 = new ArrayList<>();
        for (ServiceWrapper serviceWrapper : serviceWrappers) {
            ServiceWrapper serviceWrapper1 = CloneUtil.clone(serviceWrapper);
            serviceWrapper1.setService(null);
            ServiceRegisterBean serviceRegisterBean = new ServiceRegisterBean();
            serviceRegisterBean.setVisible(serviceWrapper1.isVisible());
            serviceRegisterBean.setServiceClass(serviceWrapper.getService().getClass().getInterfaces()[0]);
            serviceWrapper1.setServiceRegisterBean(serviceRegisterBean);
            PropertyBean propertyBean=this.getPropertyBean();
            ServiceMessage serviceMessage = new ServiceMessage(propertyBean.getIp(),propertyBean.getPort(),propertyBean.getPrefix(),ServerEnum.OUTER);
            serviceWrapper1.setServiceMessage(serviceMessage);
            serviceWrappers1.add(serviceWrapper1);
        }
        return serviceWrappers1;
    }
}
