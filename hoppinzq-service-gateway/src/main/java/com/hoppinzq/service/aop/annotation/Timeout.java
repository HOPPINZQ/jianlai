package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义超时机制
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Timeout {
    int timeout() default 20000;
}
