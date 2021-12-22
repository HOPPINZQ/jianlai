package com.hoppinzq.service.config;

import com.hoppinzq.service.exception.RemotingException;

public abstract class RetryRegisterService extends RetryTemplate{

    public RetryRegisterService(){}


    public RetryRegisterService(int sleepTime) {
        super.sleepTime=sleepTime<0?0:sleepTime;
        this.alwaysRetry=Boolean.TRUE;
    }

    public RetryRegisterService(int retryCount,int sleepTime,Boolean alwaysRetry) {
        super.retryCount=retryCount<0?0:retryCount;
        super.sleepTime=sleepTime<0?0:sleepTime;
        this.alwaysRetry=alwaysRetry?Boolean.TRUE:Boolean.FALSE;
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
                //总是重试，直到成功
                if(alwaysRetry){
                    i=0;
                }
                return toDo();
            } catch (Throwable e) {
                if(e instanceof RemotingException &&e.getMessage().indexOf("java.net.ConnectException")!=-1){
                    if(alwaysRetry){
                        logger.error("不能连接到注册中心，将会一直重新注册,重试第:  "+ (i+1) +" 次");
                    }else{
                        logger.error("不能连接到注册中心，将会重新注册" +retryCount+"次,重试第:  "+ (i+1) +" 次");
                        e.printStackTrace();//限制次数可以打印保存，因为如果不限制尝试次数还打印报错就日志太多了
                    }
                    Thread.sleep(sleepTime);
                }else{
                    e.printStackTrace();
                }
                if(!alwaysRetry&&i==retryCount-1){
                    logger.error("注册服务到注册中心失败,将不再进行重试！");
                    return false;
                }
            }
        }
        return false;
    }
}
