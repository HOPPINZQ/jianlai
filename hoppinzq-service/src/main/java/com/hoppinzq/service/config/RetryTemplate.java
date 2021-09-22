package com.hoppinzq.service.config;

import com.hoppinzq.service.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ZhangQi
 **/
public abstract class RetryTemplate extends TaskTemplate{

    private static Logger logger = LoggerFactory.getLogger(RetryTemplate.class);


    private static final int DEFAULT_RETRY_COUNT = 0;

    private int retryCount = DEFAULT_RETRY_COUNT;

    // reset sleep time
    private int sleepTime = 0;

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
     * 业务重试，失败时抛出异常；
     * 是否通过返回状态重试；
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
