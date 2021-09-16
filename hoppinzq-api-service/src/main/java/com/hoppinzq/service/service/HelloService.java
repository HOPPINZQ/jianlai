package com.hoppinzq.service.service;


import com.hoppinzq.service.service.TestBean;

import java.io.IOException;
import java.io.InputStream;

public interface HelloService {
    public String sayHello(String name);
    public String sendLargeStream(InputStream in) throws IOException;
    TestBean testChange(TestBean testBean);
}
