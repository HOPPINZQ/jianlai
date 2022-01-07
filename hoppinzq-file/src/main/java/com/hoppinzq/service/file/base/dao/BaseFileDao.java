package com.hoppinzq.service.file.base.dao;

import com.hoppinzq.service.bean.FileEnitiy;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseFileDao {

    @Insert("<script>" +
            "insert into file" +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "   <if test=\"file_id != null and file_id != ''\">file_id,</if>" +
            "   <if test=\"file_name != null and file_name != ''\">file_name,</if>" +
            "   <if test=\"file_path != null and file_path != ''\">file_path,</if>" +
            "   <if test=\"file_date != null and file_date != ''\">file_date,</if>" +
            "   <if test=\"file_like != null and file_like != ''\">file_like,</if>" +
            "   <if test=\"file_description != null and file_description != ''\">file_description,</if>" +
            "   <if test=\"file_download != null and file_download != ''\">file_download,</if>" +
            "   <if test=\"file_name_change != null and file_name_change != ''\">file_name_change,</if>" +
            "   <if test=\"file_type != null and file_type != ''\">file_type,</if>" +
            "   <if test=\"file_volume != null and file_volume != ''\">file_volume,</if>" +
            "   <if test=\"file_version != null \">file_version,</if>" +
            "</trim>" +
            "<trim prefix='values (' suffix=')' suffixOverrides=','>" +
            "<if test=\"file_id != null and file_id != ''\">#{file_id},</if>" +
            "<if test=\"file_name != null and file_name != ''\">#{file_name},</if>" +
            "<if test=\"file_path != null and file_path != ''\">#{file_path},</if>" +
            "<if test=\"file_date != null and file_date != ''\">#{file_date},</if>" +
            "<if test=\"file_like != null and file_like != ''\">#{file_like},</if>" +
            "<if test=\"file_description != null and file_description != ''\">#{file_description},</if>" +
            "<if test=\"file_download != null and file_download != ''\">#{file_download},</if>" +
            "<if test=\"file_name_change != null and file_name_change != ''\">#{file_name_change},</if>" +
            "<if test=\"file_type != null and file_type != ''\">#{file_type},</if>" +
            "<if test=\"file_volume != null and file_volume != ''\">#{file_volume},</if>" +
            "<if test=\"file_version != null\">#{file_version},</if>" +
            "</trim>"+
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "file_id", keyColumn = "file_id")
    void insertFile(FileEnitiy enitiy);

    @Select("select * from file where file_id=#{file_id}")
    Map queryFileById(String file_id);

    @Select("select * from file order by file_download desc LIMIT 0,5")
    List<Map> queryFile();

    @Update("update file set file_download=#{file_download} where file_id = #{file_id}")
    void updateFile(FileEnitiy enitiy);

}
