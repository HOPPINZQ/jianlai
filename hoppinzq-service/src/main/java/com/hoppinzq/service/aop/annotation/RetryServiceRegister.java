package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryServiceRegister {
    /**
     * retryCount
     * @return
     */
    int count() default 0;

    /**
     * retry interval Time
     * @return
     */
    int sleep() default 0;

}
