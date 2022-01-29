package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.exception.UserException;

public interface LoginService {
    JSONObject login(User user);
    void login_not_ky(User user);
    void logout();
    void logout(String token);
    void sendMobileLogin(User user) throws UserException ;
    User getUserByCode(String ucode);
    User getUserByToken(String token);
    void register(User user) throws UserException;
    void registerBySms(User user) throws UserException;
    void registerByEmail(User user) throws UserException;
    int test();
    User getUser();
}
