package com.hoppinzq.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ZhangQi
 * 重试机制模板，捕获异常将进行重试（你也通过捕获toDo方法返回原方法返回值里的响应码）。
 * 默认10次重试，每分钟尝试执行一次
 **/
public abstract class RetryTemplate extends TaskTemplate{

    protected static Logger logger = LoggerFactory.getLogger(RetryTemplate.class);

    private static final int DEFAULT_RETRY_COUNT = 10;

    protected int retryCount = DEFAULT_RETRY_COUNT;

    protected int sleepTime = 60000;//1min

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

    @Override
    public Object execute() throws InterruptedException {
        return null;
    }

}
