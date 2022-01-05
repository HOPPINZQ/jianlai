package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.bean.BlogVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogDao {

    @Select("SELECT * FROM blog_class ORDER BY parent_id")
    List<Map> queryBlogClass();

    @Insert("<script>" +
            "replace into blog" +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "<if test=\"id != null and id != ''\">id,</if>" +
            "<if test=\"title != null and title != ''\">title,</if>" +
            "<if test=\"description != null and description != ''\">description,</if>" +
            "<if test=\"buildType != null\">build_type,</if>" +
            "<if test=\"csdnLink != null and csdnLink != ''\">csdn_link,</if>" +
            "<if test=\"text != null and text != ''\">text,</if>" +
            "<if test=\"blogLike != null\">blog_like,</if>" +
            "<if test=\"star != null\">star,</if>" +
            "<if test=\"collect != null\">collect,</if>" +
            "<if test=\"author != null and author != ''\">author,</if>" +
            "<if test=\"authorName != null and authorName != ''\">author_name,</if>" +
            "<if test=\"createTime != null\">create_time,</if>" +
            "<if test=\"updateTime != null\">update_time,</if>" +
            "<if test=\"file != null and file != ''\">file,</if>" +
            "<if test=\"fileId != null and fileId != ''\">file_id,</if>" +
            "<if test=\"isComment != null\">is_comment,</if>" +
            "<if test=\"blogClass != null and blogClass != ''\">blog_class,</if>" +
            "<if test=\"isCreateSelf != null\">is_create_self,</if>" +
            "<if test=\"musicFile != null and musicFile != ''\">music_file,</if>" +
            "<if test=\"image != null and image != ''\">image,</if>" +
            "<if test=\"html != null and html != ''\">html,</if>" +
            "<if test=\"copyLink != null and copyLink != ''\">copy_link,</if>" +
            "<if test=\"type != null\">type,</if>" +
            "<if test=\"blogClassName != null and blogClassName != ''\">blog_class_name,</if>" +
            "</trim>" +
            "<trim prefix='values (' suffix=')' suffixOverrides=','>" +
            "   <if test=\"id != null and id != ''\">#{id},</if>" +
            "   <if test=\"title != null and title != ''\">#{title},</if>" +
            "   <if test=\"description != null and description != ''\">#{description},</if>" +
            "   <if test=\"buildType != null\">#{buildType},</if>" +
            "   <if test=\"csdnLink != null and csdnLink != ''\">#{csdnLink},</if>" +
            "   <if test=\"text != null and text != ''\">#{text},</if>" +
            "   <if test=\"blogLike != null\">#{blogLike},</if>" +
            "   <if test=\"star != null\">#{star},</if>" +
            "   <if test=\"collect != null\">#{collect},</if>" +
            "   <if test=\"author != null and author != ''\">#{author},</if>" +
            "   <if test=\"authorName != null and authorName != ''\">#{authorName},</if>" +
            "   <if test=\"createTime != null\">#{createTime},</if>" +
            "   <if test=\"updateTime != null\">#{updateTime},</if>" +
            "   <if test=\"file != null and file != ''\">#{file},</if>" +
            "   <if test=\"fileId != null and fileId != ''\">#{fileId},</if>" +
            "   <if test=\"isComment != null\">#{isComment},</if>" +
            "   <if test=\"blogClass != null and blogClass != ''\">#{blogClass},</if>" +
            "   <if test=\"isCreateSelf != null\">#{isCreateSelf},</if>" +
            "   <if test=\"musicFile != null and musicFile != ''\">#{musicFile},</if>" +
            "   <if test=\"image != null and image != ''\">#{image},</if>" +
            "   <if test=\"html != null and html != ''\">#{html},</if>" +
            "   <if test=\"copyLink != null and copyLink != ''\">#{copyLink},</if>" +
            "   <if test=\"type != null\">#{type},</if>" +
            "   <if test=\"blogClassName != null and blogClassName != ''\">#{blogClassName},</if>" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertOrUpdateBlog(Blog entity);

    List<Blog> queryBlog(@Param(value = "blog") BlogVo blogVo);
    int countBlog(@Param(value = "blog") BlogVo blogVo);

    void updateBlog(Blog blog);

    @Delete("delete from blog where id = #{id}")
    void deleteBlog(String id);

    @Insert("insert into csdn_error_link(url,user) values(#{url},#{userId})")
    void insertErrorLinkCSDN(String url,String userId);

}
