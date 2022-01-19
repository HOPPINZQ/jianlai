package com.hoppinzq.service.core;

import com.hoppinzq.service.aop.annotation.*;
import com.hoppinzq.service.bean.ServiceApiBean;
import com.hoppinzq.service.bean.ServiceMethodApiBean;
import com.hoppinzq.service.cache.apiCache;
import com.hoppinzq.service.util.AopTargetUtil;
import com.hoppinzq.service.util.StringUtil;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * api注册中心
 * 该类在启动后会马上装配被注解环绕的类，应该稍微加快点速度
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

    public ApiStore(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean containsApi(String apiName, String version) {
        return apiMap.containsKey(apiName + "_" + version);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
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
                        isAnnotation = true;
                        String apiServiceTitle =  apiServiceMapping.title();
                        String apiServiceDescription = apiServiceMapping.description();
                        serviceApiBean.apiServiceTitle=apiServiceTitle;
                        serviceApiBean.apiServiceDescription = apiServiceDescription;
                        ServiceMethodApiBean serviceMethodApiBean = new ServiceMethodApiBean();

                        //幂等注解，是否幂等
                        AutoIdempotent idempotent=m.getAnnotation(AutoIdempotent.class);
                        if(idempotent!=null){
                            serviceMethodApiBean.tokenCheck=true;
                        }

                        //返回值是否封装注解，————
                        ReturnTypeUseDefault returnTypeUseDefault=m.getAnnotation(ReturnTypeUseDefault.class);
                        if(returnTypeUseDefault==null){
                            serviceMethodApiBean.methodReturn=apiMapping.returnType();
                        }else{
                            serviceMethodApiBean.methodReturn=false;
                        }

                        //缓存注解，是否缓存
                        ApiCache apiCache=m.getAnnotation(ApiCache.class);
                        if(apiCache!=null){
                            serviceMethodApiBean.isCache=true;
                            serviceMethodApiBean.cacheTime=apiCache.time();
                        }

                        //权限
                        ApiMapping.RoleType rightType=apiMapping.roleType();
                        if(apiServiceMapping.roleType()==ApiServiceMapping.RoleType.NO_RIGHT){
                            rightType=ApiMapping.RoleType.NO_RIGHT;
                        }else if(apiServiceMapping.roleType()==ApiServiceMapping.RoleType.ALL_RIGHT){
                            rightType=ApiMapping.RoleType.LOGIN;
                        }else if(apiServiceMapping.roleType()==ApiServiceMapping.RoleType.ALL_ADMIN_RIGHT){
                            rightType=ApiMapping.RoleType.ADMIN;
                        }

                        serviceMethodApiBean.methodRight=rightType;
                        serviceMethodApiBean.methodTitle=apiMapping.title();
                        serviceMethodApiBean.methodDescription=apiMapping.description();
                        serviceMethodApiBean.serviceMethod=apiMapping.value();
                        //判断服务接口的value是否重复，如果有重复的不让启动
                        //使用断言暴力终止，你可以通过Spring自带的actuator去优雅的终止
                        //使用System.exit(n);会造成死锁
                        //这是因为我发现SpringApplicationShutdownHook处于BLOCKED状态，这个应该就是关闭Spring钩子函数被阻塞
                        //主线程自然处于WAITING状态，可能的原因是某个线程持有锁但是没释放，LOCK.lock();会一直等待获取锁，导致阻塞
                        Assert.isTrue(checkServiceIsE(serviceMethodApiBean.serviceMethod,methodList,type),
                                StringUtil.isNotEmpty(serviceMethodApiBean.serviceMethod)?
                                        "在类："+type.getName()+"里发现重复的服务接口的value值："+serviceMethodApiBean.serviceMethod:"在类："+type.getName()+"里发现有接口服务注册的服务名不存在");

                        ApiMapping.Type requestType=apiMapping.type();
                        serviceMethodApiBean.requestType=requestType;

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
                            //参数带有泛型的情况，这里我没处理，考虑的情况太多了qaq todo
                            //泛型实际只在编译时起作用，是不是我也可以不去考虑了呢？
                            Type[] actualTypeArguments = ((ParameterizedType)genericReturnType).getActualTypeArguments();
                            for(Type actualTypeArgument: actualTypeArguments) {
                                //System.err.println(actualTypeArgument);
                            }
                        }catch (Exception ex){
                            //ex.printStackTrace();
                        }

                        serviceMethodApiBean.serviceMethodReturn=genericReturnType;
                        try {
                            //先只考虑void跟基本数据类型，实体类直接打印实体类名，先不去打印里面字段了
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
     * 检查要注册的接口服务是否已存在
     * @param method
     * @param methodList
     * @return
     */
    private Boolean checkServiceIsE(String method,List<ServiceMethodApiBean> methodList,Class type){
        if(!StringUtil.isNotEmpty(method)){
            logger.error("启动失败！原因是:在类"+type.getName()+"里发现有接口服务注册的服务名不存在");
            return false;
        }
        for(ServiceMethodApiBean serviceMethodApiBean:methodList){
            if(method.equals(serviceMethodApiBean.getServiceMethod())){
                logger.error("启动失败！原因是:在类"+type.getName()+"里发现重复的接口服务注册，服务名是:"+method);
                return false;
            }
        }
        return true;
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
}
