package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义重试注解：
 * 重试次数与间隔时间
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int count() default 10;
    int sleep() default 6000;
}
