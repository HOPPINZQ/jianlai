package com.hoppinzq.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author:ZhangQi
 **/
public class CloneUtil {

    /**
     * 序列化对象克隆，要克隆的对象必须实现序列化接口
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T clone(T obj) {
        T clone = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            ObjectInputStream ios = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            clone = (T) ios.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clone;
    }
}
