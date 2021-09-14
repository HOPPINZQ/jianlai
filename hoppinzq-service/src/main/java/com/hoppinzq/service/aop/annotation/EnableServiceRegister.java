package com.hoppinzq.service.aop.annotation;

import com.hoppinzq.service.config.ServletRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServletRegister.class)
public @interface EnableServiceRegister {

}
