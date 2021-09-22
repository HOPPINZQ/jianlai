package com.hoppinzq.service.interfaceService;

import com.hoppinzq.service.bean.TestBean;

import java.io.IOException;
import java.io.InputStream;

public interface HelloService {
    public String sayHello(String name);
    public String sendLargeStream(InputStream in) throws IOException;
    TestBean testChange(TestBean testBean);
    void testChangeWithoutReturn(TestBean testBean);
}
