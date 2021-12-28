package com.hoppinzq.service.interfaceService;

import com.hoppinzq.service.bean.User;

public interface LoginService {
    void login(User user);
    void logout();
    User getUserByToken(String token);
    void register(User user);
    int test();
    User getUser();
}
