package com.hoppinzq.service.interfaceService;

import com.hoppinzq.service.bean.User;

import java.io.Serializable;

public interface LoginTestService extends Serializable {

    String getToken(User user);
}
