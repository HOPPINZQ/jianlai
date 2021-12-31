package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.bean.BlogVo;
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
            "<if test=\"buildType != null\">build_type,</if>" +
            "<if test=\"csdnLink != null and csdnLink != ''\">csdn_link,</if>" +
            "<if test=\"text != null and text != ''\">text,</if>" +
            "<if test=\"blogLike != null\">blogLike,</if>" +
            "<if test=\"star != null\">star,</if>" +
            "<if test=\"collect != null\">collect,</if>" +
            "<if test=\"author != null and author != ''\">author,</if>" +
            "<if test=\"createTime != null and createTime != ''\">create_time,</if>" +
            "<if test=\"updateTime != null and updateTime != ''\">update_time,</if>" +
            "<if test=\"file != null and file != ''\">file,</if>" +
            "<if test=\"isComment != null\">is_comment,</if>" +
            "<if test=\"Class != null and _class != ''\">_class,</if>" +
            "<if test=\"isCreateSelf != null\">is_create_self,</if>" +
            "<if test=\"musicFile != null and musicFile != ''\">music_file,</if>" +
            "<if test=\"image != null and image != ''\">image,</if>" +
            "<if test=\"html != null and html != ''\">html,</if>" +
            "<if test=\"copyLink != null and copyLink != ''\">copy_link,</if>" +
            "<if test=\"type != null\">type,</if>" +
            "<if test=\"ClassName != null and ClassName != ''\">_class_name,</if>" +
            "</trim>" +
            "<trim prefix='values (' suffix=')' suffixOverrides=','>" +
            "   <if test=\"id != null and id != ''\">#{id},</if>" +
            "   <if test=\"title != null and title != ''\">#{title},</if>" +
            "   <if test=\"description != null and description != ''\">#{description},</if>" +
            "   <if test=\"buildType != null\">#{build_type},</if>" +
            "   <if test=\"csdnLink != null and csdnLink != ''\">#{csdn_link},</if>" +
            "   <if test=\"text != null and text != ''\">#{text},</if>" +
            "   <if test=\"blogLike != null\">#{blog_like},</if>" +
            "   <if test=\"star != null\">#{star},</if>" +
            "   <if test=\"collect != null\">#{collect},</if>" +
            "   <if test=\"author != null and author != ''\">#{author},</if>" +
            "   <if test=\"createTime != null and createTime != ''\">#{create_time},</if>" +
            "   <if test=\"updateTime != null and updateTime != ''\">#{update_time},</if>" +
            "   <if test=\"file != null and file != ''\">#{file},</if>" +
            "   <if test=\"isComment != null\">#{isComment},</if>" +
            "   <if test=\"Class != null and Class != ''\">#{_class},</if>" +
            "   <if test=\"isCreateSelf != null\">#{is_create_self},</if>" +
            "   <if test=\"musicFile != null and musicFile != ''\">#{music_file},</if>" +
            "   <if test=\"image != null and image != ''\">#{image},</if>" +
            "   <if test=\"html != null and html != ''\">#{html},</if>" +
            "   <if test=\"copyLink != null and copyLink != ''\">#{copy_link},</if>" +
            "   <if test=\"type != null\">#{type},</if>" +
            "   <if test=\"ClassName != null and ClassName != ''\">#{_class_name},</if>" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertBlog(Blog entity);

    List<Blog> queryBlog(@Param(value = "blog") BlogVo blogVo);
    int countBlog(@Param(value = "blog") BlogVo blogVo);

    void updateBlog(Blog blog);

    @Delete("delete from blog where id = #{id}")
    void deleteBlog(String id);

    @Insert("insert into csdn_error_link(url,user) values(#{url},#{userId})")
    void insertErrorLinkCSDN(String url,String userId);

}
