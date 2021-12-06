package com.hoppinzq.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogDao {

    @Select("SELECT * FROM blog_class")
    List<Map> queryBlogClass();


}
