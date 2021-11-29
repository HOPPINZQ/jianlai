package com.hoppinzq.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlogDao {

    @Select("selc")
    public String qwe();
}
