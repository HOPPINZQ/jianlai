package com.hoppinzq.service.server;

import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.aop.annotation.Timeout;
import org.springframework.stereotype.Service;

/**
 * @author:ZhangQi
 **/
@ServiceRegister
@Service
public class TestServiceImpl implements TestService{

    @Timeout
    public String test(String str) {
        return str;
    }
}
