package com.hoppinzq.service.aop;

import com.hoppinzq.service.aop.annotation.Retry;
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
 * 重试注解织入代码
 **/
@Aspect
@Configuration
public class RetryAspect {

    @Pointcut("@annotation(com.hoppinzq.service.aop.annotation.Retry)")
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
        Retry retry = method.getAnnotation(Retry.class);
        retryTemplate.setRetryCount(retry.count()).setSleepTime(retry.sleep());
        return retryTemplate.execute();
    }

}

