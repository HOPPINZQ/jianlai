package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int count() default 0;
    int sleep() default 0;
}
