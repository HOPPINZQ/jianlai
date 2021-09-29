package com.hoppinzq.service.listen;

import com.hoppinzq.service.task.TaskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author:ZhangQi
 * 用于监听SpringBoot项目是否启动成功，启动成功将任务队列里的方法按序执行
 **/
@Component
public class ServiceRegisterListener implements ApplicationListener<AvailabilityChangeEvent> {

    private static Logger logger = LoggerFactory.getLogger(ServiceRegisterListener.class);

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        if (ReadinessState.ACCEPTING_TRAFFIC == event.getState()){
            if(!TaskStore.taskQueue.isEmpty()){
                logger.info("应用启动完成，开始向注册中心注册服务");
                try{
                    Object o=TaskStore.taskQueue.pop().execute();//返回true表示注册成功，目前注册无顺序且只有一个
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
