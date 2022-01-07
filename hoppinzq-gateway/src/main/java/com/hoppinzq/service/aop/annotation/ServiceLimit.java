package com.hoppinzq.service.aop.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 自定义注解限流
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented
@Order(2)
public  @interface ServiceLimit {
	/**
	 * 描述
	 */
	String description()  default "";

	/**
	 * key
	 */
	String key() default "";

    /**
     * 1s几次限制，小于等于0不会限制，最多每秒5次触发限制
     * @return
     */
    int number() default 5;

	/**
	 * 类型
	 */
	LimitType limitType() default LimitType.CUSTOMER;

	enum LimitType {
		/**
		 * 自定义key
		 */
		CUSTOMER,
		/**
		 * 根据请求者IP限流
		 */
		IP
	}
}
