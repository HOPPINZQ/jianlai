package com.hoppinzq.service;

import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.client.ServiceProxyFactory;
import com.hoppinzq.service.exception.RemotingException;
import com.hoppinzq.service.service.HeartbeatService;
import com.hoppinzq.service.service.ServiceWrapper;
import com.hoppinzq.service.servlet.SpringProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerCenterApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(ServerCenterApplication.class, args);
        new CheckServiceIsAvailableThread().start();//启动检查服务是否可用线程
    }

}
class CheckServiceIsAvailableThread extends Thread{
    private static Logger logger = LoggerFactory.getLogger(CheckServiceIsAvailableThread.class);
    public void run(){
        while(true){
            try {
                if(ServiceStore.heartbeatService.size()>0){
                    for(ServiceWrapper serviceWrapper: ServiceStore.heartbeatService){
                        if(serviceWrapper.isAvailable()){
                            String ip=serviceWrapper.getServiceMessage().getServiceIP();
                            String serviceIp=ip;
                            try{
                                HeartbeatService service = ServiceProxyFactory.createProxy(HeartbeatService.class, "http://"+ip+":8802/service");
                                serviceIp=service.areYouOk();
                            }catch (RemotingException ex){
                                for(ServiceWrapper serviceWrapper1:ServiceStore.serviceWrapperList){
                                    if(serviceWrapper1.getService()==null&&serviceIp.equals(serviceWrapper1.getServiceMessage().getServiceIP())){
                                        serviceWrapper1.setAvailable(Boolean.FALSE);
                                    }
                                }
                                logger.error("检测到IP为"+serviceIp+"的服务已不可用");
                            }
                        }
                    }
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}