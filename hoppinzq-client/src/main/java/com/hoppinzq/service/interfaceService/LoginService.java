package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.User;
import org.omg.CORBA.UserException;

public interface LoginService {
    User login(User user);
    void login_not_ky(User user);
    void logout();
    void logout(String token);
    User getUserByCode(String ucode);
    User getUserByToken(String token);
    void register(User user) throws UserException;
    void registerBySms(User user) throws com.hoppinzq.service.exception.UserException;
    void registerByEmail(User user) throws UserException;
    int test();
    User getUser();
}
