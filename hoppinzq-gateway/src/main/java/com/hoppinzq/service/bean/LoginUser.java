package com.hoppinzq.service.bean;

import java.io.Serializable;

/**
 * @author: zq
 */
public class LoginUser {

    public static ThreadLocal<Serializable> userHold = new ThreadLocal<Serializable>();
    /**
     * 获取设置的主体，可以强转成设置主体的类
     * @return
     */
    public static final Serializable getUserHold() {
        return userHold.get();
    }

    /**
     * 可以设置一个主体，但是必须要有下面的字段，你可以把下面的字段放在一个实体类里，然后用此法设置之
     * @param serializable
     */
    public static final void setUserHold(Serializable serializable) {
        userHold.set(serializable);
    }

    /**
     * 线程结束清空
     */
    public static void exit() {
        userHold.set(null);
    }

    /**
     * 线程开始初始化
     */
    public static void enter() {
        userHold.set(null);
    }
}
