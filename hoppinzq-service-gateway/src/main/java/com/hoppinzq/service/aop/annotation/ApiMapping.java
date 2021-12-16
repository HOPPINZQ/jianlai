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

    /**
     * 权限类型
     */
    ApiMapping.RoleType roleType() default ApiMapping.RoleType.NO_RIGHT;

    enum RoleType {
        NO_RIGHT,//不校验权限
        LOGIN,//校验登录权限
        MEMBER//校验登录权限及会员权限
    }

    /**
     * 权限类型
     */
    boolean returnType() default true;

}
