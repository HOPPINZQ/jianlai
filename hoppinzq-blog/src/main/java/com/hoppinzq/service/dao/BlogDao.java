package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogDao {

    @Select("SELECT * FROM blog_class")
    List<Map> queryBlogClass();

    @Insert("<script>" +
            "insert into blog" +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "<if test=\"id != null and id != ''\">id,</if>" +
            "<if test=\"title != null and title != ''\">title,</if>" +
            "<if test=\"description != null and description != ''\">description,</if>" +
            "<if test=\"build_type != null\">build_type,</if>" +
            "<if test=\"csdn_link != null and csdn_link != ''\">csdn_link,</if>" +
            "<if test=\"text != null and text != ''\">text,</if>" +
            "<if test=\"blog_like != null\">blog_like,</if>" +
            "<if test=\"star != null\">star,</if>" +
            "<if test=\"collect != null\">collect,</if>" +
            "<if test=\"author != null and author != ''\">author,</if>" +
            "<if test=\"create_time != null and create_time != ''\">create_time,</if>" +
            "<if test=\"update_time != null and update_time != ''\">update_time,</if>" +
            "<if test=\"file != null and file != ''\">file,</if>" +
            "<if test=\"is_comment != null\">is_comment,</if>" +
            "<if test=\"_class != null and _class != ''\">_class,</if>" +
            "<if test=\"is_create_self != null\">is_create_self,</if>" +
            "<if test=\"music_file != null and music_file != ''\">music_file,</if>" +
            "<if test=\"image != null and image != ''\">image,</if>" +
            "<if test=\"html != null and html != ''\">html,</if>" +
            "<if test=\"copy_link != null and copy_link != ''\">copy_link,</if>" +
            "<if test=\"type != null\">type,</if>" +
            "</trim>" +
            "<trim prefix='values (' suffix=')' suffixOverrides=','>" +
            "   <if test=\"id != null and id != ''\">#{id},</if>" +
            "   <if test=\"title != null and title != ''\">#{title},</if>" +
            "   <if test=\"description != null and description != ''\">#{description},</if>" +
            "   <if test=\"build_type != null\">#{build_type},</if>" +
            "   <if test=\"csdn_link != null and csdn_link != ''\">#{csdn_link},</if>" +
            "   <if test=\"text != null and text != ''\">#{text},</if>" +
            "   <if test=\"blog_like != null\">#{blog_like},</if>" +
            "   <if test=\"star != null\">#{star},</if>" +
            "   <if test=\"collect != null\">#{collect},</if>" +
            "   <if test=\"author != null and author != ''\">#{author},</if>" +
            "   <if test=\"create_time != null and create_time != ''\">#{create_time},</if>" +
            "   <if test=\"update_time != null and update_time != ''\">#{update_time},</if>" +
            "   <if test=\"file != null and file != ''\">#{file},</if>" +
            "   <if test=\"is_comment != null\">#{is_comment},</if>" +
            "   <if test=\"_class != null and _class != ''\">#{_class},</if>" +
            "   <if test=\"is_create_self != null\">#{is_create_self},</if>" +
            "   <if test=\"music_file != null and music_file != ''\">#{music_file},</if>" +
            "   <if test=\"image != null and image != ''\">#{image},</if>" +
            "   <if test=\"html != null and html != ''\">#{html},</if>" +
            "   <if test=\"copy_link != null and copy_link != ''\">#{copy_link},</if>" +
            "   <if test=\"type != null\">#{type},</if>" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertBlog(Blog entity);


    @Select("<script>select * from blog as blog" +
            "<where>" +
            "<if test=\"blog != null\">" +
            "<if test=\"blog.type != null\">" +
            "and blog.type=#{blog.type}" +
            "</if>" +
            "</if>" +
            "</where></script>")
    List<Blog> queryBlog(@Param(value = "blog") Map map);

    void updateBlog(Blog blog);

    @Delete("delete from blog where id = #{id}")
    void deleteBlog(String id);

    @Insert("insert into csdn_error_link(url,user) values(#{url},#{userId})")
    void insertErrorLinkCSDN(String url,String userId);

}
