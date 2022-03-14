package com.hoppinzq.service.aop.service;

import com.hoppinzq.service.aop.dao.LogDao;
import com.hoppinzq.service.bean.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    LogDao logDao;

    @Async
    public void saveRequestInfo(RequestInfo record){
        logDao.insertLog(record);
//        try { TimeUnit.SECONDS.sleep(10); ;
//            System.err.println(record.getIp());
//        } catch (InterruptedException ie){}
    }
}
