package com.hoppinzq.service;

import com.hoppinzq.service.common.InvocationRequest;

import java.io.InputStream;
import java.lang.reflect.Method;

public interface TransportSession {
    InputStream sendInvocationRequest(Method method, InvocationRequest request, InputStream streamArgument) throws Exception;
}
