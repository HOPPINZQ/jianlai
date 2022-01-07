package com.hoppinzq.service.aop;

import com.hoppinzq.service.aop.annotation.Timeout;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author:ZhangQi
 **/
@Aspect
@Configuration
public class TimeoutAspect {

    @Pointcut(value = "@annotation(com.hoppinzq.service.aop.annotation.Timeout)")
    public void pointCut() {
    }


    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Exception {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Timeout timeoutCut = method.getDeclaredAnnotation(Timeout.class);
        int timeout=timeoutCut.timeout();
        ExecutorService executor = Executors.newCachedThreadPool();
        Future future = executor.submit(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return "操作失败";
            }
        });
        long t = System.currentTimeMillis();
        while (true) {
            Thread.sleep(1000);
            if (System.currentTimeMillis() - t >= timeout) {
                throw new RuntimeException("调用超时,响应时间超过调用该服务配置的超时时长:"+timeout+"ms");
            }
            if (future.isDone()) {
                return future.get();
            }
        }
    }
}

