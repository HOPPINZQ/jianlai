package com.hoppinzq.service.bean;

import java.io.Serializable;

/**
 * @author: zq
 */
public class WebMessageContext {
    private static InheritableThreadLocal<Serializable> principalHolder = new InheritableThreadLocal<Serializable>();

    public static final Serializable getPrincipal() {
        return principalHolder.get();
    }

    public static void exit() {
        principalHolder.set(null);
    }

    public static void enter(Serializable principal) {
        principalHolder.set(principal);
    }
}
