package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.SpiderLink;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}
