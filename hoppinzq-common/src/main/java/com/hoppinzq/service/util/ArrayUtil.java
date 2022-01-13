package com.hoppinzq.service.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zq
 */
public class ArrayUtil {

    private ArrayUtil() {
    }

    public Object[] deleteArrayNull(Object[] string) {
        Object[] array = string;
        List<Object> list = new ArrayList<>(array.length);
        for (Object str : array) {
            list.add(str);
        }
        while (list.remove(null)) ;
        Object[] list2 = list.toArray(new Object[list.size()]);
        return list2;
    }
}
