package com.hoppinzq.service.core;

import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.bean.ServiceApiBean;
import com.hoppinzq.service.bean.ServiceMethodApiBean;
import com.hoppinzq.service.cache.apiCache;
import com.hoppinzq.service.util.AopTargetUtil;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.*;
import java.util.*;


/**
 * api注册中心
 * @author:ZhangQi
 */
public class ApiStore {
    private static ApplicationContext applicationContext;
    private static Logger logger = LoggerFactory.getLogger(ApiStore.class);

    /**
     * API 接口存储map
     */
    private Map<String, ApiRunnable> apiMap = apiCache.apiMap;
    private static List<ServiceApiBean> outApiList = apiCache.outApiList;
    /**
     * @param applicationContext
     */
    public ApiStore(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 加载所有bean,扫描api网关注解并存储
     */
    public void loadApiFromSpringBeans() {
        logger.debug("开始为网关注册接口");
        String[] names = applicationContext.getBeanDefinitionNames();
        Class<?> type;
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            if (bean == this) {
                continue;//不扫描本类
            }
            //获取代理增强前的bean
            if (bean instanceof Advised) {
                try{
                    bean= AopTargetUtil.getTarget(bean);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                type=bean.getClass();
            }else{
                type = applicationContext.getType(name);
            }

            ApiServiceMapping apiServiceMapping = type.getAnnotation(ApiServiceMapping.class);
            if(apiServiceMapping!=null){
                ServiceApiBean serviceApiBean = new ServiceApiBean();
                List<ServiceMethodApiBean> methodList = new ArrayList();
                Boolean isAnnotation = false;
                for (Method m : type.getDeclaredMethods()) {
                    // 通过反射拿到APIMapping注解
                    ApiMapping apiMapping = m.getAnnotation(ApiMapping.class);
                    if (apiMapping != null) {
                        ApiMapping.RoleType rightType=apiMapping.roleType();
                        boolean returnType=apiMapping.returnType();
                        if(apiServiceMapping.roleType()==ApiServiceMapping.RoleType.NO_RIGHT){
                            rightType=ApiMapping.RoleType.NO_RIGHT;
                        }else if(apiServiceMapping.roleType()==ApiServiceMapping.RoleType.ALL_RIGHT){
                            rightType=ApiMapping.RoleType.LOGIN;
                        }
                        isAnnotation = true;
                        String apiServiceTitle =  apiServiceMapping.title();
                        String apiServicDescription = apiServiceMapping.description();
                        serviceApiBean.apiServiceTitle=apiServiceTitle;
                        serviceApiBean.apiServiceDescription = apiServicDescription;
                        ServiceMethodApiBean serviceMethodApiBean = new ServiceMethodApiBean();
                        serviceMethodApiBean.methodRight=rightType;
                        serviceMethodApiBean.methodReturn=returnType;
                        serviceMethodApiBean.methodTitle=apiMapping.title();
                        serviceMethodApiBean.methodDescription=apiMapping.description();
                        serviceMethodApiBean.serviceMethod=apiMapping.value();
                        LocalVariableTableParameterNameDiscoverer u =
                                new LocalVariableTableParameterNameDiscoverer();
                        String[] params = u.getParameterNames(m);
                        List array = new ArrayList();
                        for (int i = 0; i < params.length; i++) {
                            Map object = new HashMap();
                            object.put("serviceMethodParamType", m.getParameterTypes()[i].getCanonicalName());
                            object.put("serviceMethodParamTypeParams", getBeanFileds(m.getParameterTypes()[i]));
                            object.put("serviceMethodParamName", params[i]);
                            array.add(object);
                        }
                        serviceMethodApiBean.serviceMethodParams=array;
                        Type genericReturnType=m.getGenericReturnType();
                        try{
                            //泛型 todo
                            Type[] actualTypeArguments = ((ParameterizedType)genericReturnType).getActualTypeArguments();
                            for(Type actualTypeArgument: actualTypeArguments) {
                                //System.err.println(actualTypeArgument);
                            }
                        }catch (Exception ex){
                            //ex.printStackTrace();
                        }

                        serviceMethodApiBean.serviceMethodReturn=genericReturnType;
                        try {
                            if("void".equals(genericReturnType.getTypeName())){
                                serviceMethodApiBean.serviceMethodReturnParams="void";
                            }else{
                                serviceMethodApiBean.serviceMethodReturnParams=getBeanFileds(Class.forName(genericReturnType.getTypeName()));
                            }
                        } catch (ClassNotFoundException ex) {
                            serviceMethodApiBean.serviceMethodReturnParams=genericReturnType.getTypeName();
                            //throw new RuntimeException("没有找到类："+genericReturnType.getTypeName());
                        }
                        methodList.add(serviceMethodApiBean);
                        addApiItem(apiMapping, name, m,serviceMethodApiBean);
                    }
                }
                if (isAnnotation) {
                    serviceApiBean.serviceMethods=methodList;
                    outApiList.add(serviceApiBean);
                }
            }
        }
    }

    /**
     * 获取bean的通知对象
     * @param advised
     * @param className
     * @return
     */
    private Advice findAdvice(Advised advised, String className) {
        for (Advisor a : advised.getAdvisors()) {
            if (a.getAdvice().getClass().getName().equals(className)) {
                return a.getAdvice();
            }
        }
        return null;
    }

    /**
     * 获取类的参数列表
     * @param beanClass
     * @return
     */
    private static List<Map> getBeanFileds(Class beanClass) {
        List<Map> list = new ArrayList<Map>();
        //判断是否是基本数据类型或String 等
        if(!(isPrimitiveWrapClass(beanClass)||
                ("java.lang.String".equals(beanClass.getName())))){
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                Map map = new HashMap();
                map.put("beanParamName", field.getName());
                map.put("beanParamType", field.getType());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 判断是否是基本数据类型或者是包装类型
     * @param clz
     * @return
     */
    private static boolean isPrimitiveWrapClass(Class clz) {
        try {
            if(clz.isPrimitive()){
                return true;
            }
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }



    /**
     * 查找api
     *
     * @param apiName
     * @return
     */
    public ApiRunnable findApiRunnable(String apiName) {
        return apiMap.get(apiName);
    }

    /**
     * 添加api
     *
     * @param apiMapping api配置
     * @param beanName   beanq在spring context中的名称
     * @param method
     */
    private void addApiItem(ApiMapping apiMapping, String beanName, Method method,ServiceMethodApiBean serviceMethodApiBean) {
        ApiRunnable apiRun = new ApiRunnable();
        apiRun.apiName = apiMapping.value();
        apiRun.targetMethod = method;
        apiRun.targetName = beanName;
        apiRun.serviceMethodApiBean=serviceMethodApiBean;
        apiMap.put(apiMapping.value(), apiRun);
    }

    public ApiRunnable findApiRunnable(String apiName, String version) {
        return (ApiRunnable) apiMap.get(apiName + "_" + version);
    }

    public List<ApiRunnable> findApiRunnables(String apiName) {
        if (apiName == null) {
            throw new IllegalArgumentException("api name 不能为空!");
        }
        List<ApiRunnable> list = new ArrayList<ApiRunnable>(20);
        for (ApiRunnable api : apiMap.values()) {
            if (api.apiName.equals(apiName)) {
                list.add(api);
            }
        }
        return list;
    }

    public List<ApiRunnable> getAll() {
        List<ApiRunnable> list = new ArrayList<ApiRunnable>(20);
        list.addAll(apiMap.values());
        Collections.sort(list, new Comparator<ApiRunnable>() {
            @Override
            public int compare(ApiRunnable o1, ApiRunnable o2) {
                return o1.getApiName().compareTo(o2.getApiName());
            }
        });
        return list;
    }

    public boolean containsApi(String apiName, String version) {
        return apiMap.containsKey(apiName + "_" + version);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
