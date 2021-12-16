package com.hoppinzq.service.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author:ZhangQi
 **/
@Component
public class InjectSelfPostProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(InjectSelfPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Set<Method> selfMethods = this.findAllSelfAnnotatedMethods(bean);
        if (!selfMethods.isEmpty()) {
            injectSelf(selfMethods, bean);
        }
        return bean;
    }

    Set<Method> findAllSelfAnnotatedMethods(Object bean) throws BeansException {
        Class clazz = bean.getClass();
        Set<Method> methods = findAllSelfAnnotatedMethods(clazz);
        return methods;
    }

    Set<Method> findAllSelfAnnotatedMethods(Class clazz) throws BeansException {
        Set<Method> selfMethods = new HashSet<Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!isAnnotatedSelf(method)) {
                continue;
            }
            if (method.getParameterTypes().length != 1) {
                continue;
            }
            if (!method.getName().startsWith("set")) {
                continue;
            }
            selfMethods.add(method);
        }
        if (clazz.getSuperclass() != null) {
            selfMethods.addAll(this.findAllSelfAnnotatedMethods(clazz.getSuperclass()));
        }
        return selfMethods;
    }

    boolean isAnnotatedSelf(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations == null) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Self.class)) {
                return true;
            }
        }
        return false;
    }

    void injectSelf(Set<Method> selfMethods, Object bean) {
        for (Method method : selfMethods) {
            try {
                method.invoke(bean, bean);
                logger.info("Injected self, class:" + bean.getClass().getName() + ", method: " + method.getName());
            } catch (IllegalAccessException e) {
                throw new FatalBeanException("Fail to inject self", e);
            } catch (InvocationTargetException e) {
                throw new FatalBeanException("Fail to inject self", e);
            }
        }
    }
}
