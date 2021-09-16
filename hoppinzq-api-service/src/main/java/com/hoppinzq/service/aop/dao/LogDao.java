package com.hoppinzq.service.aop.dao;

import com.hoppinzq.service.vo.RequestInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogDao {

    @Insert("insert into my_log(classMethod,httpMethod,createTime,logLevel,requestParams" +
            ",result,ip,url,exception,timeCost) values(#{log.classMethod}," +
            "'GET',#{log.createTime},#{log.logLevel},#{log.requestParams}," +
            "#{log.result},#{log.ip},#{log.url},#{log.exception},#{log.timeCost})")
    void insertLog(@Param(value = "log") RequestInfo requestInfo);
}
