package com.hoppinzq.service.dao;

import com.hoppinzq.service.bean.CmsContentPO;
import org.apache.ibatis.annotations.Insert;

public interface CrawlerMapper {
    @Insert(" insert into cms_content (contentId,\n" +
            "                                 title,\n" +
            "                                 releaseDate,\n" +
            "                                 content)\n" +
            "        values (#{contentId,jdbcType=VARCHAR},\n" +
            "                #{title,jdbcType=VARCHAR},\n" +
            "                #{releaseDate,jdbcType=TIMESTAMP},\n" +
            "                #{content,jdbcType=LONGVARCHAR})")
    int addCmsContent(CmsContentPO record);
}
