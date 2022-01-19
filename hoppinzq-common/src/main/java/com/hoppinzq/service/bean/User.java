package com.hoppinzq.service.bean;

import com.hoppinzq.service.util.EncryptUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 */
public class User implements  Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String description;

    private Date create;

    private Date update;

    private String image;

    private int userright;

    private int code;//可以是邮箱验证码，也可以是手机验证码，一般是手机验证码
    private String token;
    private int isRemember=0;//默认是0，不记住用户名密码

    private int state;
    private String login_type;
    private Object extra_message;//额外的信息

    public Object getExtra_message() {
        return extra_message;
    }

    public void setExtra_message(Object extra_message) {
        this.extra_message = extra_message;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsRemember() {
        return isRemember;
    }

    public void setIsRemember(int isRemember) {
        this.isRemember = isRemember;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
