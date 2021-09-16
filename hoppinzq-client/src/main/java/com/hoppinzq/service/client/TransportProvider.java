package com.hoppinzq.service.client;

public interface TransportProvider<T extends TransportSession> {

    T createSession(MethodInvocationHandler invocationHandler);

    void endSession(T session, MethodInvocationHandler invocationHandler);
}
