package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解服务注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiServiceMapping {
    String title() default "";

    String description() default "";
}
