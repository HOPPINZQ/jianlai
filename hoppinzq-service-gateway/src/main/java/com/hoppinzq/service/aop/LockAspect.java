package com.hoppinzq.service.aop;

import com.hoppinzq.service.aop.annotation.Servicelock;
import com.hoppinzq.service.exception.ResultReturnException;
import com.hoppinzq.service.util.AopGetParamUtil;
import com.hoppinzq.service.util.EncryptUtil;
import com.hoppinzq.service.util.IPUtils;
import com.hoppinzq.service.util.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步锁 AOP
 * @author:ZhangQi
 */
@Component
@Scope
@Aspect
@Order(3)
//order越小越是最先执行，最先执行的最后结束。order默认值是2147483647
public class LockAspect {
	/**
	 * 为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
	private static Lock lock = new ReentrantLock(true);//互斥锁 参数默认false，不公平锁

	@Pointcut("@annotation(com.hoppinzq.service.aop.annotation.Servicelock)")
	public void lockAspect() {
		
	}
	
    @Around("lockAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Servicelock servicelock = method.getAnnotation(Servicelock.class);
        Object obj = null;
        if(servicelock.lockType()==Servicelock.LockType.DEFAULT){
            lock.lock();
            try {
                //获取入参
                Map map= AopGetParamUtil.getRequestParamsByJoinPoint(joinPoint);
                String json= JSONUtil.transMapToString(map);

                obj = joinPoint.proceed();
            }catch (ResultReturnException e){
                throw new ResultReturnException(e.getMsg(),403,e);
            } catch (Throwable e) {
                throw new ResultReturnException("数据处理中，请稍后",403,e);
            } finally{
                lock.unlock();
            }
            return obj;
        }else{
            try{
                joinPoint.proceed();
            } catch (Throwable e) {
                throw new ResultReturnException(e.getMessage(),403,e);
            }
        }
        return obj;
    }
}
