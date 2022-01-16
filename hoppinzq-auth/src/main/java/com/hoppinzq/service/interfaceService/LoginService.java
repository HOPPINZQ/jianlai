package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.common.UserException;

public interface LoginService {
    JSONObject login(User user);
    void logout();
    void logout(String token);
    User getUserByToken(String token);
    void register(User user) throws UserException;
    void registerByEmail(User user) throws UserException;
    void registerByMobile(User user);
    int test();
    User getUser();
}
