package com.hoppinzq.service.common;

import java.io.Serializable;

/**
 * 可以保存用户名/密码组合的类
 * 可以定制
 */
public class UserPrincipal implements Serializable {
    private String username;
    private String password;

    public UserPrincipal() {
    }

    public UserPrincipal(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return "Username: " + username + "\nPassword: " + password;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return true;
    }

    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return getClass().getCanonicalName();
    }
}
