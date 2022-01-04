package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.BlogVo;
import com.hoppinzq.service.bean.Comment;
import com.hoppinzq.service.bean.CommentVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    @Insert("<script>" +
            "replace into blog" +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "<if test=\"id != null\">id,</if>" +
            "<if test=\"author != null and author != ''\">author,</if>" +
            "<if test=\"comment != null and comment != ''\">comment,</if>" +
            "<if test=\"date != null\">date,</if>" +
            "<if test=\"blogId != null and blogId != ''\">blog_id,</if>" +
            "<if test=\"pid != null\">pid,</if>" +
            "<if test=\"like != null\">like,</if>" +
            "</trim>" +
            "<trim prefix='values (' suffix=')' suffixOverrides=','>" +
            "   <if test=\"id != null\">#{id},</if>" +
            "   <if test=\"author != null and author != ''\">#{author},</if>" +
            "   <if test=\"comment != null and comment != ''\">#{comment},</if>" +
            "   <if test=\"date != null\">#{date},</if>" +
            "   <if test=\"blogId != null and blogId != ''\">#{blog_id},</if>" +
            "   <if test=\"pid != null\">#{pid},</if>" +
            "   <if test=\"like != null\">#{like},</if>" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertOrUpdateBlogComment(Comment entity);

    List<Comment> queryComment(@Param(value = "comment") CommentVo commentVo);
    int countBlog(@Param(value = "comment") CommentVo commentVo);


    @Delete("delete from comment where id = #{id}")
    void deleteComment(int id);
}
