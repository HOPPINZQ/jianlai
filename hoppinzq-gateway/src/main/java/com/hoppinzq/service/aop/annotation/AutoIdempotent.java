package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：幂等注解，校验参数里的token
 * 只会在被ApiServiceMapping环绕的类中和被ApiMapping环绕的方法上生效
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoIdempotent {
}
