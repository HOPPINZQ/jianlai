package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：不封装返回值
 * 只会在被ApiServiceMapping环绕的类和被ApiMapping环绕的方法上生效
 * 优先级优于ApiMapping注解的returnType，也就是说如果方法有该注解，强行以该注解为主
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReturnTypeUseDefault {
}
