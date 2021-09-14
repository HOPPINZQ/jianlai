package com.hoppinzq.service.aop;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.hoppinzq.service.aop.annotation.ServiceLimit;
import com.hoppinzq.service.exception.ResultReturnException;
import com.hoppinzq.service.util.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 限流 AOP
 * @author:ZhangQi
 */
@Aspect
@Configuration
public class LimitAspect implements InitializingBean, Ordered {

    private static List<LoadingCache<String, RateLimiter>> cacheList = new ArrayList<>();
    @Override
    public int getOrder() {
        return 1;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        //根据IP分不同的令牌桶, 每天自动清理缓存
        //预先初始化5个
        cacheList.add(CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String key) {
                        // 每秒只发出1个令牌
                        return RateLimiter.create(1);
                    }
                }));
        cacheList.add(CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String key) {
                        // 每秒只发出2个令牌
                        return RateLimiter.create(2);
                    }
                }));
        cacheList.add(CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String key) {
                        // 每秒只发出3个令牌
                        return RateLimiter.create(3);
                    }
                }));
        cacheList.add(CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String key) {
                        // 每秒只发出4个令牌
                        return RateLimiter.create(4);
                    }
                }));
        cacheList.add(CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String key) {
                        // 每秒只发出5个令牌
                        return RateLimiter.create(5);
                    }
                }));
    }


    //Service层切点  限流
    @Pointcut("@annotation(com.hoppinzq.service.aop.annotation.ServiceLimit)")
    public void ServiceAspect() {

    }

    @Around("ServiceAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ServiceLimit limitAnnotation = method.getAnnotation(ServiceLimit.class);
        Object obj;
        int number = limitAnnotation.number();//限制次数
        if (number <= 0) {
            obj = joinPoint.proceed();
            return obj;
        } else {
            if (number >= 6) {
                number = 5;
            }
            ServiceLimit.LimitType limitType = limitAnnotation.limitType();
            String key = limitAnnotation.key();
            if (limitType.equals(ServiceLimit.LimitType.IP)) {
                key = IPUtils.getIpAddr();
                //降级策略

            }
            RateLimiter rateLimiter = cacheList.get(number - 1).get(key);
            Boolean flag = rateLimiter.tryAcquire();
            if (flag) {
                obj = joinPoint.proceed();
            } else {
                throw new ResultReturnException("请求频繁,请稍后调用", 403);
            }
        }
        return obj;
    }
}
