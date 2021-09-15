package com.hoppinzq.service.aop.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

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
}
