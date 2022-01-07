package com.hoppinzq.service.common;

import java.io.Serializable;

/**
 * 调用服务请求的响应
 */
public class InvocationResponse implements Serializable {

    /**
     * 远程服务方法本身引发的异常
     */
    private Throwable exception;

    /**
     * 如果没有引发异常，则方法调用的结果
     */
    private Serializable result;

    /**
     * 执行远程操作时服务器上发生的修改列表
     */
    private ModificationList[] modifications;

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Serializable getResult() {
        return result;
    }

    public void setResult(Serializable result) {
        this.result = result;
    }

    public ModificationList[] getModifications() {
        return modifications;
    }

    public void setModifications(ModificationList[] modifications) {
        this.modifications = modifications;
    }
}
