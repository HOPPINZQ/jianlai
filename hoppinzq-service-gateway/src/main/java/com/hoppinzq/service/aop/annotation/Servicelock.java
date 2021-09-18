package com.hoppinzq.service.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解同步锁
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented
public @interface Servicelock {
	 String description()  default "";
	/**
	 * 类型
	 */
	Servicelock.LockType lockType() default LockType.DEFAULT;

	enum LockType {
		DEFAULT,
		SELECT,
		INSERT,
		DELETE,
		UPDATE
	}
}
