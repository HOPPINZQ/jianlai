package com.hoppinzq.service.aop.annotation;

import com.hoppinzq.service.plugin.ServiceCacheFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServiceCacheFilter.class)
public @interface EnableServiceCache {

}
