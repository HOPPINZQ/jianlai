package com.hoppinzq.service.exception;

/**
 * 远程服务调用自定义异常
 */
public class RemotingException extends RuntimeException {
    public RemotingException(Exception e) {
        super(e);
    }
}
