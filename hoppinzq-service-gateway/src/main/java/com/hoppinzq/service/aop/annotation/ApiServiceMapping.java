package com.hoppinzq.service.aop.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 自定义注解服务注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ApiServiceMapping {
    @AliasFor(
            annotation = Service.class
    )
    String value() default "";
    String title() default "";
    String description() default "";
}
