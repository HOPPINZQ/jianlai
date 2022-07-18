package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.SpiderLink;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SpiderDao {

    @Insert("<script>" +
            " insert into spider (title,link) " +
            "    VALUES" +
            "    <foreach collection='list' item='spiderLinks' separator=','>" +
            "        ( #{spiderLinks.title}, #{spiderLinks.link})" +
            "    </foreach>" +
            "</script>")
    void insertSpiderLink(List<SpiderLink> spiderLinks);

    @Select("select * from spider")
    List<Map> queryAllLink();

    @Select("select * from spider limit #{index} , #{count}")
    List<Map> queryLink(int index,int count);
}
