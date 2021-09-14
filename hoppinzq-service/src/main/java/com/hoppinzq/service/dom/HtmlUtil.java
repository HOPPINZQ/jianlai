package com.hoppinzq.service.dom;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author:ZhangQi
 **/
public class HtmlUtil {

    private HtmlUtil(){}

    public static StringBuilder createDom(StringBuilder stringBuilder,String dom){
        if(stringBuilder==null){
            stringBuilder=new StringBuilder();
        }
        stringBuilder.append(domWapper(dom));
        return stringBuilder;
    }

    public static StringBuilder createDom(StringBuilder stringBuilder,String dom,String var){
        if(stringBuilder==null){
            return new StringBuilder();
        }
        stringBuilder.append(domWapper(dom)).append(var).append(domWapper(dom,false));
        return stringBuilder;
    }

    private static String domWapper(String dom){
        return "<"+dom+">";
    }

    private static String domWapper(String dom,Boolean isStart){
        if(!isStart){
            return "</"+dom+">";
        }else{
            return "<"+dom+">";
        }
    }


    /**
     * @param objectList
     * @param <E>
     * @return
     */
    private static <E> HashSet<String> getFieldsHasValue(Collection<E> objectList) {
        HashSet<String> newSet = new HashSet<>();
        if (objectList != null && objectList.size() > 0) {
            List<Field> fields = null;
            for (E e : objectList) {
                if (fields == null) {
                    fields = getAllFields(e, null);
                }

                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object val = field.get(e);
                        if (val != null) {
                            newSet.add(field.getName());
                        }
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        return newSet;
    }

    /**
     * 获取class的字段，包括父类的属性
     *
     * @param object
     * @param expectedFieldSet 期望的字段集合，null：返回所有
     * @return
     */
    private static List<Field> getAllFields(Object object, Set<String> expectedFieldSet) {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        List<Field> resultList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        if (expectedFieldSet != null) {
            fieldList.forEach(f -> {
                if (expectedFieldSet.contains(f.getName())) {
                    resultList.add(f);
                }
            });
        } else {
            resultList.addAll(fieldList);
        }

        return resultList;
    }
}
