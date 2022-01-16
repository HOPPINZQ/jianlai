package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {

    @Insert("<script>" +
            "insert into blog_user" +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "<if test=\"id != null and id != ''\">id,</if>" +
            "<if test=\"username != null and username != ''\">username,</if>" +
            "<if test=\"password != null and password != ''\">password,</if>" +
            "<if test=\"userright != null\">userright,</if>" +
            "<if test=\"phone != null and phone != ''\">phone,</if>" +
            "<if test=\"description != null and description != ''\">description,</if>" +
            "<if test=\"email != null and email != ''\">email,</if>" +
            "<if test=\"image != null and image != ''\">image,</if>" +
            "<if test=\"create != null and create != ''\">create,</if>" +
            "<if test=\"update != null and update != ''\">update,</if>" +
            "</trim>" +
            "<trim prefix='values (' suffix=')' suffixOverrides=','>" +
            "   <if test=\"id != null and id != ''\">#{id},</if>" +
            "   <if test=\"username != null and username != ''\">#{username},</if>" +
            "   <if test=\"password != null and password != ''\">#{password},</if>" +
            "   <if test=\"userright != null\">#{userright},</if>" +
            "   <if test=\"phone != null and phone != ''\">#{phone},</if>" +
            "   <if test=\"description != null and description != ''\">#{description},</if>" +
            "   <if test=\"email != null and email != ''\">#{email},</if>" +
            "   <if test=\"image != null and image != ''\">#{image},</if>" +
            "   <if test=\"create != null and create != ''\">#{create},</if>" +
            "   <if test=\"update != null and update != ''\">#{update},</if>" +
            "</trim>" +
            "</script>")
    void createUser(User user);

    List<User> queryUser(User user);

    int isUser(String username,String email,String phone);
}
