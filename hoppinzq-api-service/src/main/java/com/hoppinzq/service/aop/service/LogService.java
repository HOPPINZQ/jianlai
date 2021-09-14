package com.hoppinzq.service.aop.service;

import com.hoppinzq.service.vo.RequestInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

//    @Autowired
//    LogDao logDao;

    @Async
    public void saveRequestInfo(RequestInfo record){
//        try { TimeUnit.SECONDS.sleep(10); ;
//            System.err.println(record.getIp());
//        } catch (InterruptedException ie){}
    }
}
