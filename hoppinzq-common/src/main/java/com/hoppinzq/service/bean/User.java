package com.hoppinzq.service.bean;

import com.hoppinzq.service.util.EncryptUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 */
public class User implements  Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String description;

    private Date create;

    private Date update;

    private String image;

    private int userright;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserright() {
        return userright;
    }

    public void setUserright(int userright) {
        this.userright = userright;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void MD5encode(){
        this.setPassword(EncryptUtil.MD5(this.password));
    }

}
