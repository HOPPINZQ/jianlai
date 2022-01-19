package com.hoppinzq.service.bean;

import java.io.Serializable;

/**
 * @author: ZhangQi
 */
public class RequestContext {
    public static ThreadLocal<Serializable> requestHold = new ThreadLocal<Serializable>();

    public static final Serializable getPrincipal() {
        return requestHold.get();
    }
    public static final void setRequestHold(Serializable serializable) {
        requestHold.set(serializable);
    }

    public static void exit() {
        requestHold.set(null);
    }

    public static void enter(Serializable principal) {
        requestHold.set(principal);
    }
}
