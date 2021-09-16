package com.hoppinzq.service.aop.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义代理注解，该注解的类必须实现自定义代理接口，并代理指定类下的指定方法
 * Todo 未完成
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ServiceClassPoint {
    @AliasFor(
        annotation = Component.class
    )
    Class<?> type();

    String method();

    Class<?>[] args();

    ProxyType poxyType() default ProxyType.CGLIB;

    enum ProxyType {
        /**
         * cglib代理,默认
         */
        CGLIB,
        /**
         * jdk代理
         */
        JDK
    }
}
