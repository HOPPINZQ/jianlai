package com.hoppinzq.service.config;

import com.hoppinzq.service.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ZhangQi
 * 重试机制模板，捕获异常将进行重试（你也通过捕获toDo方法返回原方法返回值里的响应码）。
 * 默认10次重试，每分钟尝试执行一次
 **/
public abstract class RetryTemplate extends TaskTemplate{

    private static Logger logger = LoggerFactory.getLogger(RetryTemplate.class);


    private static final int DEFAULT_RETRY_COUNT = 10;

    private int retryCount = DEFAULT_RETRY_COUNT;

    private int sleepTime = 60000;//1min

    public RetryTemplate(){}
    public RetryTemplate(int retryCount,int sleepTime){
        this.retryCount=retryCount<0?0:retryCount;
        this.sleepTime=sleepTime<0?0:sleepTime;
    }


    public int getSleepTime() {
        return sleepTime;
    }

    public RetryTemplate setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime<0?0:sleepTime;
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public RetryTemplate setRetryCount(int retryCount) {
        this.retryCount = retryCount<0?0:retryCount;
        return this;
    }

    /**
     * 业务重试，失败时抛出异常
     * 通过返回状态重试
     * @return
     */
    protected abstract Object toDo() throws Throwable;

    @Override
    public Object execute() throws InterruptedException {
        for (int i = 0; i < retryCount; i++) {
            try {
                return toDo();
            } catch (Throwable e) {
                if(e instanceof RemotingException&&e.getMessage().indexOf("java.net.ConnectException")!=-1){
                    logger.error("不能连接到注册中心，将会重新注册,重试第:  "+ (i+1) +" 次");
                    e.printStackTrace();
                    Thread.sleep(sleepTime);
                }
                if(i==retryCount-1){
                    logger.error("注册服务到注册中心失败,将不再进行重试！");
                    return false;
                }
            }
        }
        return false;
    }

}
