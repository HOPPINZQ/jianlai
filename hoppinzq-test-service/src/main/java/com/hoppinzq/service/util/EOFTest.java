package com.hoppinzq.service.util;

import java.io.*;

/**
 * @author:ZhangQi
 * 将实体类序列化至流里，获取流反序列化得到实体类
 **/
public class EOFTest {
    public static void main(String[] args) throws Exception {
        User user1 = new User("zhangqi", 24);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(user1);
        oos.writeObject(null);//在流的最后写入一个null

        byte[] data = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o=null;
        while ((o=ois.readObject())!=null){//根据写入的内容，当读取到null时，标志流结束
            System.out.println(o.getClass().getName());//获取对象类名
            System.out.println(o);//尝试调用流中User对象的toString方法
        }
    }
}

class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public int age;

    @Override
    public String toString() {
        return "name=" + name + ", age=" + age ;
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}