package com.hoppinzq.service.aop.annotation;


import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ServiceRegister {
    @AliasFor(
        annotation = Component.class
    )
    String value() default "";
    String title() default "";
    String description() default "";
    int timeout() default 5000;

    RegisterType registerType() default RegisterType.AUTO;

    enum RegisterType {
        /**
         * 项目启动自动注册
         */
        AUTO,
        /**
         * 不会随着项目启动注册，声明服务需要手动注册
         */
        NOT_AUTO
    }

}

