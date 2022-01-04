package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.RequestInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlogLogDao {


    @Insert("insert into my_log(classMethod,httpMethod,createTime,logLevel,requestParams" +
            ",result,ip,url,exception,timeCost) values(#{log.classMethod}," +
            "#{log.httpMethod},#{log.createTime},#{log.logLevel},#{log.requestParams}," +
            "#{log.result},#{log.ip},#{log.url},#{log.exception},#{log.timeCost})")
    void insertLog(@Param(value = "log") RequestInfo requestInfo);
}
