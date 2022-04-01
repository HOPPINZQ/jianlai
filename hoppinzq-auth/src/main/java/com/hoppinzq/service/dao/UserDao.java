package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserDao {

    @Insert("<script>" +
            "replace into blog_user" +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "<if test=\"id != null and id != ''\">id,</if>" +
            "<if test=\"username != null and username != ''\">username,</if>" +
            "<if test=\"password != null and password != ''\">password,</if>" +
            "<if test=\"userright != null\">userright,</if>" +
            "<if test=\"phone != null and phone != ''\">phone,</if>" +
            "<if test=\"description != null and description != ''\">description,</if>" +
            "<if test=\"email != null and email != ''\">email,</if>" +
            "<if test=\"image != null and image != ''\">image,</if>" +
            "<if test=\"create != null\">create,</if>" +
            "<if test=\"update != null\">update,</if>" +
            "<if test=\"state != null\">state,</if>" +
            "<if test=\"login_type != null and login_type != ''\">login_type,</if>" +
            "<if test=\"extra_message != null and extra_message != ''\">extra_message,</if>" +
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
            "   <if test=\"create != null\">#{create},</if>" +
            "   <if test=\"update != null\">#{update},</if>" +
            "   <if test=\"state != null\">#{state},</if>" +
            "   <if test=\"login_type != null and login_type != ''\">#{login_type},</if>" +
            "   <if test=\"extra_message != null and extra_message != ''\">#{extra_message},</if>" +
            "</trim>" +
            "</script>")
    void insertOrUpdateUser(User user);

    List<User> queryUser(User user);

    int isUser(String username,String email,String phone);

    @Update("update blog_user set state = #{state} where username =#{userName} or phone = #{phone}")
    void userActiveChange(String userName,String phone,int state);
    @Update("update blog_user set state = #{state} where id =#{id}")
    void userActiveChange(Long id,int state);
}
