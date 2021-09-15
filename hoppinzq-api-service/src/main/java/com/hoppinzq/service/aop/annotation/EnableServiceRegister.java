package com.hoppinzq.service.aop.annotation;

import com.hoppinzq.service.config.ServiceRegisterToCore;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServiceRegisterToCore.class)
public @interface EnableServiceRegister {

}
