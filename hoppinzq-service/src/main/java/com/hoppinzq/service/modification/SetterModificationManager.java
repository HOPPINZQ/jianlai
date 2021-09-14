package com.hoppinzq.service.modification;

import com.hoppinzq.service.common.ModificationList;
import org.springframework.aop.framework.ProxyFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * @author:ZhangQi
 * 代理每个调用参数对象来跟踪对对象的更改
 * 以及跟踪在服务执行时调用的setter的更改。
 */
public class SetterModificationManager implements ModificationManager, Serializable {
    private static final Integer INITIAL_DEPTH = 1;
    private static final Integer DEFAULT_DEPTH = 10;

    private Integer depth;
    private List<String> proxyClassDefinitions;
    public ModificationList[] modifications;

    public SetterModificationManager() {
        depth = DEFAULT_DEPTH;
        proxyClassDefinitions = Collections.EMPTY_LIST;
    }

    public SetterModificationManager(Integer depth, List<String> proxyClassDefinitions) {
        this.depth = depth;
        this.proxyClassDefinitions = proxyClassDefinitions != null ? proxyClassDefinitions : Collections.EMPTY_LIST;
    }

    public Object[] applyModificationScheme(Object[] objects) {
        if (objects == null) return null;
        modifications = new ModificationList[objects.length];
        Object[] proxiedObjects = new Object[objects.length];

        for (int index = 0; index < objects.length; index++) {
            Object object = objects[index];
            ModificationList modificationList = new ModificationList();
            modifications[index] = modificationList;
            proxiedObjects[index] = createSetterProxy(INITIAL_DEPTH, object, "", modificationList);
        }

        return proxiedObjects;
    }

    private Object createSetterProxy(Integer currentDepth, Object object, String parentPath, ModificationList modificationList) {
        ProxyFactory pf = new ProxyFactory(object);
        pf.setProxyTargetClass(true);
        pf.addAdvice(new SetterModificationInterceptor(parentPath, modificationList));
        Object newProxyInstance = pf.getProxy();

        currentDepth++;

        if (currentDepth <= depth) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (classShouldBeProxied(field.getType())) {
                    Object childObject = getChildObject(field, object);
                    if (childObject != null) {
                        String path = ("".equals(parentPath) ? field.getName() :  ("." + field.getName()));
                        Object proxiedChild = createSetterProxy(currentDepth, childObject, path, modificationList);
                        setChildObject(field, object, proxiedChild);
                    }
                }
            }
        }

        return newProxyInstance;
    }

    private void setChildObject(Field field, Object object, Object proxiedChild) {
        try {
            field.setAccessible(true);
            field.set(object, proxiedChild);
        } catch (IllegalAccessException ignored) {
        }
    }

    private Object getChildObject(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }

    private boolean classShouldBeProxied(Class candidate) {
        for (String classDefinition : proxyClassDefinitions) {
            if (candidate.getCanonicalName().startsWith(classDefinition.replace("*", "")))
                return true;
        }
        return false;
    }

    public ModificationList[] getModifications() {
        removeShadowingModifications();
        return modifications;
    }

    private void removeShadowingModifications() {

    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public List<String> getProxyClassDefinitions() {
        return proxyClassDefinitions;
    }

    public void setProxyClassDefinitions(List<String> proxyClassDefinitions) {
        this.proxyClassDefinitions = proxyClassDefinitions;
    }
}