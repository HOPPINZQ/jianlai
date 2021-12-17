package com.hoppinzq.service;

public interface TransportProvider<T extends TransportSession> {

    T createSession(MethodInvocationHandler invocationHandler);

    void endSession(T session, MethodInvocationHandler invocationHandler);
}
