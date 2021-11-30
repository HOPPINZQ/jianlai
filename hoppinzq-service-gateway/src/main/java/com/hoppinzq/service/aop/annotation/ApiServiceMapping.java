package com.hoppinzq.service.aop.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 自定义注解：注册服务类到Spring容器和服务类信息
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
