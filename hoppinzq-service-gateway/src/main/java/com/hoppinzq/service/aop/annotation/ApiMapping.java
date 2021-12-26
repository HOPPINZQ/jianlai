package com.hoppinzq.service.aop.annotation;


import java.lang.annotation.*;

/**
 * 自定义注解：要暴露的服务类方法
 * 只会在被ApiServiceMapping环绕的类中生效
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
     * 声明返回值是否需要网关统一封装
     * 为true表示需要统一封装，这样前端就可以统一对this.response进行处理
     * 为false是由你去封装，因为有一些前端组件已经定死了其解析规范，必须按照他的要求反参
     */
    boolean returnType() default true;

}
