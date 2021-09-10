package com.hoppinzq.service.brap.auth;

import java.io.Serializable;

/**
 * 保存已验证成功的用户在其调用服务的一个独立线程内，在该线程内的操作随时可以通过AuthenticationContext.getPrincipal取出调用方信息以存根
 * 为什么要这么做？
 * 为每一个调用服务的线程保存其独享的对象，这样每个线程都可以访问或修改自己所拥有的副本, 而不会影响其他线程的副本，确保了线程安全。
 * 这种做法在实现线程安全跟调用跟踪链很常见
 */
public class AuthenticationContext {
    private static ThreadLocal<Serializable> principalHolder = new ThreadLocal<Serializable>();

    public static final Serializable getPrincipal() {
        return principalHolder.get();
    }

    /**
     * ps:成功验证后，可以设置新的主体
     */
    public static final void setPrincipal(Serializable principal) {
        principalHolder.set(principal);
    }

    /**
     * 调用服务结束后删除该用户
     */
    public static void exit() {
        principalHolder.set(null);
    }
    /**
     * 在进行用户校验前初始化
     */
    public static void enter() {
        principalHolder.set(null);
    }
}
