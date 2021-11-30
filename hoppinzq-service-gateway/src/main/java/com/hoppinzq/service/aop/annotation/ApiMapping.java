package com.hoppinzq.service.aop.annotation;


import java.lang.annotation.*;

/**
 * 自定义注解：要暴露的服务类方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiMapping {
    String value();

    String title() default "";

    String description() default "";
}
