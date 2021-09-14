package com.hoppinzq.service.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:ZhangQi
 **/
public class MapUtil {

    /**
     * Map转Bean的方法
     */
    public static Object map2JavaBean(Class<?> clazz, Map<String, Object> map) throws Exception {
        Object javaBean = clazz.newInstance(); //构建对象
        Method[] methods = clazz.getMethods(); //获取所有方法
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String field = method.getName(); //比如"setName"
                field = field.substring(field.indexOf("set") + 3);//截取属性名"Name"
                field = field.toLowerCase().charAt(0) + field.substring(1);//name
                if (map.containsKey(field)) {
                    method.invoke(javaBean, map.get(field)); //执行setName(String name)方法
                }
            }
        }
        return javaBean;
    }

    /**
     * Bean转Map的方法
     */
    public static Map<String, Object> javaBean2Map(Object javaBean) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Method[] methods = javaBean.getClass().getMethods();//获取对象的所有方法
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String field = method.getName(); //获取方法名"getName"
                field = field.substring(field.indexOf("get") + 3);
                field = field.toLowerCase().charAt(0) + field.substring(1);//拼接属性名
                Object value = method.invoke(javaBean, (Object[]) null);
                map.put(field, value);
            }
        }
        return map;
    }
}
