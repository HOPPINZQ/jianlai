package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.SpiderBean;
import com.hoppinzq.service.bean.SpiderLink;
import com.hoppinzq.service.bean.SpiderMajor;
import org.apache.ibatis.annotations.*;

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
    List<SpiderLink> queryAllLink();

    @Select("select * from spider limit #{index} , #{count}")
    List<SpiderLink> queryLink(int index,int count);

    @Select("select * from spider where isIndex=0")
    List<SpiderLink> queryAllLinkNotIndex();

    @Update("<script>" +
            "replace into spider (id,title,link,isIndex) values " +
            "    <foreach collection='list' item='spiderLinks' separator=','>" +
            "         ( #{spiderLinks.id},#{spiderLinks.title},#{spiderLinks.link}, 1)" +
            "    </foreach>" +
            "</script>")
    void updateLinkInIndex(List<SpiderLink> spiderLinks);

    @Insert("<script>" +
            " insert into spiderminor (_mid,_key,description,xpath,selector,attr,links,addLinks,regex,isAll,xpathFunction) " +
            "    VALUES" +
            "    <foreach collection='list' item='spiderBean' separator=','>" +
            "        ( #{spiderBean.mid},#{spiderBean.key},#{spiderBean.description},#{spiderBean.xpath},#{spiderBean.selector}," +
            "#{spiderBean.attr},#{spiderBean.links},#{spiderBean.addLinks},#{spiderBean.regex},#{spiderBean.isAll},#{spiderBean.xpathFunction})" +
            "    </foreach>" +
            "</script>")
    void insertSpiders(List<SpiderBean> spiderBeans);

    @Insert("insert into spidermajor(id,name,description,urldemo,thread) values(#{sm.id},#{sm.name},#{sm.desc},#{sm.urldemo},#{sm.threadNum})")
    void insertSpiderMajor(@Param(value = "sm") SpiderMajor spiderMajor);

    @Select("select * from spidermajor where id= #{id}")
    SpiderMajor querySpiderMajorById(Long id);

    @Select("select * from spidermajor")
    List<SpiderMajor> querySpiderMajorAll();

    @Select("select * from spiderminor where _mid= #{mid}")
    List<SpiderBean> querySpidersByMid(Long mid);

    List<SpiderMajor> querySpider(@Param(value = "spider") SpiderMajor spiderMajor);

    @Insert("insert into feedback (name,contact,message) values (#{name},#{contact},#{message})")
    void insertFeedback(String name,String contact,String message);
}
