package com.hoppinzq.service.aop.annotation;

import com.hoppinzq.service.config.RetryTemplate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author:ZhangQi
 **/
@Aspect
@Configuration
public class RetryAspect {

    @Pointcut("@annotation(com.hoppinzq.service.aop.annotation.RetryServiceRegister)")
    public void retryServiceRegister() {

    }

    @Around("retryServiceRegister()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Exception {
        RetryTemplate retryTemplate = new RetryTemplate() {
            @Override
            protected Object toDo() throws Throwable {
                return joinPoint.proceed();
            }
        };
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RetryServiceRegister retryServiceRegister = method.getAnnotation(RetryServiceRegister.class);
        retryTemplate.setRetryCount(retryServiceRegister.count()).setSleepTime(retryServiceRegister.sleep());
        return retryTemplate.execute();
    }

}

