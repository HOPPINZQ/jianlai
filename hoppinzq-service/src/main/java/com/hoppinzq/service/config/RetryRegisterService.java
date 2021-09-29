package com.hoppinzq.service.config;

import com.hoppinzq.service.exception.RemotingException;

public abstract class RetryRegisterService extends RetryTemplate{

    public RetryRegisterService(){}

    public RetryRegisterService(int retryCount,int sleepTime) {
        super.retryCount=retryCount<0?0:retryCount;
        super.sleepTime=sleepTime<0?0:sleepTime;
    }

    /**
     * 必须重写该方法以进行业务重试，失败时抛出异常
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
                if(e instanceof RemotingException &&e.getMessage().indexOf("java.net.ConnectException")!=-1){
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
