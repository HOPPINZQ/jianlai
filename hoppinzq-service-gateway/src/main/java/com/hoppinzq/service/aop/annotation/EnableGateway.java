package com.hoppinzq.service.aop.annotation;

import com.hoppinzq.service.config.GatewayServletConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * zq网关开关
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GatewayServletConfig.class)
public @interface EnableGateway {
}
