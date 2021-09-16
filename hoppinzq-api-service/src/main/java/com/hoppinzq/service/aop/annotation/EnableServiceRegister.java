package com.hoppinzq.service.aop.annotation;

import com.hoppinzq.service.config.ServiceRegisterToCore;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 服务缓存注解
 * Todo 未实现
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServiceRegisterToCore.class)
public @interface EnableServiceRegister {

}
