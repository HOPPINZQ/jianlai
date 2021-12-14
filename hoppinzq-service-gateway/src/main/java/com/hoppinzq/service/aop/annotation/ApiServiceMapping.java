package com.hoppinzq.service.aop.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 自定义注解：注册服务类到Spring容器和服务类自定义信息
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

    /**
     * 权限类型
     */
    ApiServiceMapping.RoleType roleType() default ApiServiceMapping.RoleType.NO_RIGHT;

    enum RoleType {
        NO_RIGHT,//服务类下的全部方法都不校验权限
        RIGHT,//服务类下的全部方法都由方法自己决定是否校验权限（通过为ApiMapping注解设置Type的枚举值）
        ALL_RIGHT//服务类下的全部方法都校验登录权限
    }
}
