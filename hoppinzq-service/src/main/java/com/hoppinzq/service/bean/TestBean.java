package com.hoppinzq.service.bean;

import java.io.Serializable;

/**
 * @author:ZhangQi
 **/
public class TestBean implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;
    private String userName;
    private String password;
    private int age;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
