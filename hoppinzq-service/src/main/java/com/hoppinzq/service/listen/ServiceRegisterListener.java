package com.hoppinzq.service.listen;

import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.bean.PropertyBean;
import com.hoppinzq.service.bean.ServiceMessage;
import com.hoppinzq.service.bean.ServiceRegisterBean;
import com.hoppinzq.service.bean.ServiceWrapper;
import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.exception.RemotingException;
import com.hoppinzq.service.interfaceService.RegisterServer;
import com.hoppinzq.service.service.HeartbeatService;
import com.hoppinzq.service.task.TaskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:ZhangQi
 * 用于监听SpringBoot项目是否启动成功，启动成功将任务队列里的方法按序执行
 **/
@Component
public class ServiceRegisterListener implements ApplicationListener<AvailabilityChangeEvent> {

    private static Logger logger = LoggerFactory.getLogger(ServiceRegisterListener.class);

    @Autowired
    private PropertyBean propertyBean;

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        if (ReadinessState.ACCEPTING_TRAFFIC == event.getState()){
            if(!TaskStore.taskQueue.isEmpty()){
                logger.info("应用启动完成，开始向注册中心注册服务！");
                try{
                    //一开始是想一个服务作为一个任务，放在循环队列里，注册成功的服务从循序队列移除
                    //不成功的服务继续等待注册，后来注意到：要么全部注册不成功，要么全部注册成功，不存在部分的情况，就弃用了该队列的设计
                    //所以现在该队列只有一个任务，即注册所有服务的任务
                    //返回true表示全部服务注册成功
                    Object o=TaskStore.taskQueue.pop().execute();
                    if(Boolean.parseBoolean(String.valueOf(o))){
                        //如果注册成功，开辟一个线程去轮询注册中心的心跳服务，当注册中心挂掉，重新等待注册。
                        new CheckCoreServiceIsAvailableThread(propertyBean).start();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 为每一个模块注册一个心跳服务
     */
    private void registHeartbeatService(){

    }
}

class CheckCoreServiceIsAvailableThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(CheckCoreServiceIsAvailableThread.class);

    private PropertyBean propertyBean;
    private Boolean isCoreServerAvailable=true;

    public CheckCoreServiceIsAvailableThread(PropertyBean propertyBean){
        this.propertyBean=propertyBean;
    }
    public void run() {
        while (true) {
            try {
                try {
                    HeartbeatService service = ServiceProxyFactory.createProxy(HeartbeatService.class, propertyBean.getServerCenter());
                    service.areYouOk();
                    if(!isCoreServerAvailable){
                        //重新注册！
                        logger.info("注册中心已恢复服务，重新注册！");
                        UserPrincipal upp = new UserPrincipal(propertyBean.getUserName(), propertyBean.getPassword());
                        RegisterServer registerServer = ServiceProxyFactory.createProxy(RegisterServer.class, propertyBean.getServerCenter(), upp);
                        List<ServiceWrapper> serviceWrappers=modWrapper();
                        registerServer.insertServices(serviceWrappers);
                        logger.info("向注册中心注册服务成功！");
                    }
                    isCoreServerAvailable=true;
                } catch (RemotingException ex) {
                    logger.error("注册中心可能已停止服务，将尝试重新连接注册中心！");
                    isCoreServerAvailable=false;
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.error("线程意外终止，将不会重新注册！");
                e.printStackTrace();
            }
        }
    }

    public List<ServiceWrapper> modWrapper() {
        List<ServiceWrapper> serviceWrappers = ServiceStore.serviceWrapperList;
        List<ServiceWrapper> serviceWrappersCopyList = new ArrayList<>();
        for (ServiceWrapper serviceWrapper : serviceWrappers) {
            ServiceRegisterBean serviceRegisterBean = new ServiceRegisterBean();
            serviceRegisterBean.setVisible(serviceWrapper.isVisible());
            serviceRegisterBean.setServiceClass(serviceWrapper.getService().getClass().getInterfaces()[0]);                    PropertyBean propertyBean=this.propertyBean;
            ServiceMessage serviceMessage = new ServiceMessage(propertyBean.getIp(),propertyBean.getPort(),propertyBean.getPrefix(), ServerEnum.OUTER);
            ServiceWrapper serviceWrapperCopy=new ServiceWrapper(serviceWrapper.getId(),
                    null,serviceWrapper.getAuthenticationProvider(),serviceWrapper.getAuthorizationProvider(),
                    serviceWrapper.getModificationManager(),serviceMessage,serviceRegisterBean,
                    serviceWrapper.isVisible(),serviceWrapper.isAvailable(),serviceWrapper.getServiceTypeEnum());
            serviceWrappersCopyList.add(serviceWrapperCopy);
        }
        return serviceWrappersCopyList;
    }
}
